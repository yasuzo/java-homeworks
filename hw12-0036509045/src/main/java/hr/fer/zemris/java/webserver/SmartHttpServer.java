package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.exec.SmartScriptRuntimeException;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Smart script http server.
 *
 * @author Jan Capek
 */
public class SmartHttpServer {

    /**
     * Server's ip address.
     */
    private String address;
    /**
     * Server's domain name.
     */
    private String domainName;
    /**
     * Server's port.
     */
    private int port;
    /**
     * Number of worker threads the server has.
     */
    private int workerThreads;
    /**
     * Session timeout time.
     */
    private int sessionTimeout;
    /**
     * Map of supported mime types.
     */
    private Map<String, String> mimeTypes = new HashMap<>();
    /**
     * Server's main thread.
     */
    private ServerThread serverThread;
    /**
     * Thread pool used for tasks.
     */
    private ExecutorService threadPool;
    /**
     * Server's web root.
     */
    private Path documentRoot;

    /**
     * Constructs a new server configured by config file whose path is given as an argument.
     *
     * @param configFileName Path to config file.
     * @throws NullPointerException If given path is {@code null}.
     * @throws RuntimeException     If configuration file could not be read or is invalid.
     */
    public SmartHttpServer(String configFileName) {
        Objects.requireNonNull(configFileName);
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Path.of(configFileName)));
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Config file could not be read.", e);
        }
        configure(properties);
    }

    /**
     * Main server program.
     *
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program takes in one argument, path to config file.");
            return;
        }
        new SmartHttpServer(args[0]).start();
    }

    /**
     * Configures server settings with given Properties object.
     *
     * @param properties Properties object holding server configuration.
     * @throws NumberFormatException If given argument is {@code null}.
     * @throws RuntimeException      If some of the configurations are missing or invalid.
     */
    private void configure(Properties properties) {
        Objects.requireNonNull(properties);
        try {
            address = Objects.requireNonNull(properties.getProperty("server.address"));
            domainName = Objects.requireNonNull(properties.getProperty("server.domainName"));
            port = Integer.parseInt(properties.getProperty("server.port"));
            workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
            sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
            loadMimeTypes(Path.of(properties.getProperty("server.mimeConfig")));
            documentRoot = Path.of(properties.getProperty("server.documentRoot")).toAbsolutePath().normalize();
        } catch (NullPointerException | NumberFormatException e) {
            throw new RuntimeException("Invalid configuration files.");
        } catch (IOError e) {
            throw new RuntimeException("Document root cannot be accessed.");
        }
    }

    /**
     * Loads mime types from a file which path is given as an argument.
     *
     * @param p Path to a mime properties file.
     * @throws NullPointerException If given path is {@code null}.
     * @throws RuntimeException     If config file could not be read.
     */
    private void loadMimeTypes(Path p) {
        Objects.requireNonNull(p);
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(p));
        } catch (IOException e) {
            throw new RuntimeException("Mime config could not be read.");
        }
        properties.forEach((key, value) -> {
            mimeTypes.put((String) key, (String) value);
        });
    }

    /**
     * Starts a server if not already started.
     */
    protected synchronized void start() {
        if (serverThread != null) {
            return;
        }
        serverThread = new ServerThread();
        threadPool = Executors.newFixedThreadPool(workerThreads);
        serverThread.start();
    }

    /**
     * Stops the server from running.
     */
    protected synchronized void stop() {
        if (serverThread == null || serverThread.isAlive() == false) {
            return;
        }
        serverThread.kill();
        threadPool.shutdown();
    }

    /**
     * Server thread model.
     */
    protected class ServerThread extends Thread {

        /**
         * Flag that indicates to a thread if it should stop running.
         */
        private volatile boolean shouldTerminate;

        /**
         * Terminates the thread.
         */
        protected synchronized void kill() {
            serverThread.interrupt();
            shouldTerminate = true;
        }

        /**
         * Server loop.
         *
         * @throws RuntimeException In case error with either server socket or client socket occurres.
         */
        @Override
        public void run() {
            try (ServerSocket serverSocket = createServerSocket()) {
                while (true) {
                    Socket client = serverSocket.accept();
                    if (shouldTerminate) {
                        break;
                    }
                    ClientWorker clientWorker = new ClientWorker(client);
                    threadPool.submit(clientWorker);
                }
            } catch (IOException e) {
                throw new RuntimeException("IO error occurred with either server or client socket.");
            }
        }

        /**
         * Creates a new server socket and returns it.
         *
         * @return New server socket bound to the server port.
         * @throws IOException If server socket could not be opened.
         */
        private ServerSocket createServerSocket() throws IOException {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress((InetAddress) null, port));
            return serverSocket;
        }
    }

    /**
     * Client worker model. Instances of this class are used to serve clients with content.
     */
    private class ClientWorker implements Runnable, IDispatcher {
        private Socket csocket;
        private PushbackInputStream istream;
        private OutputStream ostream;
        private String version;
        private String method;
        private String fullPath;
        private String host;
        private Map<String, String> params = new HashMap<String, String>();
        private Map<String, String> tempParams = new HashMap<String, String>();
        private Map<String, String> permPrams = new HashMap<String, String>();
        private List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
        private String SID;

        /**
         * Constructs a new worker with given client socket.
         *
         * @param csocket Socket used to communicate with a client.
         * @throws NullPointerException If given socket is {@code null}.
         */
        public ClientWorker(Socket csocket) {
            super();
            this.csocket = Objects.requireNonNull(csocket);
        }

        @Override
        public void run() {
//            open stream and read header
            String header;
            try {
                istream = new PushbackInputStream(new BufferedInputStream(csocket.getInputStream()));
                ostream = new BufferedOutputStream(csocket.getOutputStream());
                header = readHeader(istream);
            } catch (IOException e) {
                return;
            }

//            extract header information
            try {
                extractHeaderInfo(header);
            } catch (Exception e) {
                sendEmptyResponse(400, "");
                return;
            }

//            extract params
            String paramString = fullPath.replaceFirst("^.*?(?:\\?|$)", "");
            parseParameters(paramString);

//            resolve requested path
            String path = fullPath.replaceFirst("\\?.*", "");
            try {
                internalDispatchRequest(path, true);
            } catch (Exception e) {
                sendEmptyResponse(500, "Unexpected error occurred.");
            }
        }

        /**
         * Sends empty response and closes client socket.
         *
         * @param code    Response code.
         * @param message Response message.
         * @throws NullPointerException If given message is {@code null}.
         */
        private void sendEmptyResponse(int code, String message) {
            RequestContext rc = new RequestContext(ostream, null, null, null);
            rc.setStatusCode(code);
            rc.setStatusText(message);
            try {
                rc.write("");
                csocket.close();
            } catch (IOException e) {
//                nothing to be done
                e.printStackTrace();
            }
        }

        /**
         * Parses url parameters from parameter string.
         *
         * @param paramString Url parameter string. String starts after '?' and extends to the end of the url.
         * @throws NullPointerException If given string is {@code null}.
         */
        private void parseParameters(String paramString) {
            Objects.requireNonNull(paramString);
            paramString = URLDecoder.decode(paramString, StandardCharsets.ISO_8859_1);
            Matcher m = Pattern.compile("([^&=]+)=([^&=]*)").matcher(paramString);
            m.results().forEach(matchResult -> {
                String var = matchResult.group(1);
                String val = matchResult.group(2);
                params.put(var, val);
            });
        }

        /**
         * Reads request header.
         *
         * @param clientIn Client's input stream. Header will be read with that.
         * @return Request header without last \r\n\r\n
         * @throws IOException          In case data could not be read.
         * @throws NullPointerException If given stream is {@code null}.
         * @throws RuntimeException     If header is invalid.
         */
        private String readHeader(PushbackInputStream clientIn) throws IOException {
            Objects.requireNonNull(clientIn);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] array = new byte[1024];
            while (true) {
                int len = clientIn.read(array, 0, clientIn.available());
                if (len == -1) {
                    break;
                }
                bos.write(array, 0, len);
                if (new String(bos.toByteArray(), 0, len, StandardCharsets.ISO_8859_1).contains("\r\n\r\n")) {
                    break;
                }
            }
            String requestData = new String(bos.toByteArray(), StandardCharsets.ISO_8859_1);
            int headerEnd = requestData.indexOf("\r\n\r\n");
            if (headerEnd == -1) {
                throw new RuntimeException("Header is missing.");
            }
            return requestData.substring(0, headerEnd);
        }

        /**
         * Extracts header information.
         *
         * @param header Header that needs to be extracted.
         * @throws NullPointerException If given header is {@code null}.
         * @throws RuntimeException     If header is invalid.
         */
        private void extractHeaderInfo(String header) {
            Objects.requireNonNull(header);

//            extract first header line
            extractFirstHeaderLine(header);

//            extract host
            Matcher m = Pattern.compile("\r\nHost:\\s?([^\\s]+)(?:\r\n|$)").matcher(header);
            if (m.find()) {
                host = m.group(1).replaceFirst(":\\d+$", "");
            } else {
                host = domainName;
            }
        }

        /**
         * Extracts first line of a header.
         *
         * @param header Header which needs to be extracted.
         * @throws NullPointerException If given header is {@code null}.
         * @throws RuntimeException     If header is invalid.
         */
        private void extractFirstHeaderLine(String header) {
            Objects.requireNonNull(header);
            Pattern p = Pattern.compile("^GET\\s([^\\s]+)\\s(HTTP/1\\.[01])\r\n");
            Matcher m = p.matcher(header);
            if (m.find() == false) {
                throw new RuntimeException("Invalid header.");
            }
            version = m.group(2);
            method = "GET";
            fullPath = m.group(1);
        }

        /**
         * Dispatches a request but internally.
         *
         * @param urlPath    Requested url.
         * @param directCall Flag that indicates if a direct call to this method has been made.
         * @throws Exception In case an error occurred while processing a request.
         */
        public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
            Path requestedPath = null;
            try {
                requestedPath = documentRoot.resolve(urlPath.substring(1)).toAbsolutePath().normalize();
            } catch (Exception e) {
                sendEmptyResponse(404, "Path not available.");
                return;
            }

//            is requested path outside of webroot dir
            if (requestedPath.toString().startsWith(documentRoot.toString()) == false) {
                sendEmptyResponse(403, "Forbidden");
                return;
            }

//            get file extension
            String fileExtension = requestedPath.getFileName().toString().replaceFirst(".*\\.", "");

//            configure request context
            RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
            rc.setStatusCode(200);

            try {
                if (fileExtension.equals("smscr")) {
                    executeSmartScript(Files.readString(requestedPath), rc);
                } else {
                    String mimeType = mimeTypes.getOrDefault(fileExtension, "application/octet-stream");
                    rc.setMimeType(mimeType);
                    rc.write(Files.readAllBytes(requestedPath));
                    csocket.close();
                }
            } catch (IOException e) {
                sendEmptyResponse(404, "Path not available.");
            }
        }

        /**
         * Executes smart script and closes the client socket.
         *
         * @param script Loaded smart script that needs to be executed.
         * @param requestContext Request context.
         * @throws NullPointerException If any of the parameters are {@code null}.
         * @throws IOException If socket could not be closed.
         */
        private void executeSmartScript(String script, RequestContext requestContext) throws IOException {
            Objects.requireNonNull(script);
            Objects.requireNonNull(requestContext);
            try {
                new SmartScriptEngine(
                        new SmartScriptParser(script).getDocumentNode(),
                        requestContext
                ).execute();
                csocket.close();
            } catch (SmartScriptRuntimeException e) {
                sendEmptyResponse(500, "Script error");
            }
        }

        @Override
        public void dispatchRequest(String urlPath) throws Exception {
            internalDispatchRequest(urlPath, false);
        }
    }
}
