package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.exec.SmartScriptRuntimeException;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
     * Thread for cleaning sessions.
     */
    private SessionCleaner sessionCleanerThread;
    /**
     * Thread pool used for tasks.
     */
    private ExecutorService threadPool;
    /**
     * Server's web root.
     */
    private Path documentRoot;
    /**
     * Web workers used to process request.
     */
    private Map<String, IWebWorker> workersMap = new HashMap<>();

    /**
     * Map of sessions.
     */
    private Map<String, SessionMapEntry> sessions = new HashMap<>();

    /**
     * Random session id generator.
     */
    private Random sessionRandom = new Random();

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
        SmartHttpServer server = new SmartHttpServer(args[0]);
        server.start();
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
            loadWorkers(Path.of(properties.getProperty("server.workers")));
        } catch (NullPointerException | NumberFormatException e) {
            throw new RuntimeException("Invalid configuration files.");
        } catch (IOError e) {
            throw new RuntimeException("Document root cannot be accessed.");
        }
    }

    /**
     * Loads workers from workers config file whose path is given as an argument.
     *
     * @param workerConfigPath Path of the workers config file.
     * @throws NullPointerException If given path is {@code null}.
     * @throws RuntimeException     If config is not readable or is invalid.
     */
    private void loadWorkers(Path workerConfigPath) {
        Objects.requireNonNull(workerConfigPath);
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(workerConfigPath));
        } catch (IOException e) {
            throw new RuntimeException("Worker's configuration file could not be read.");
        }
        properties.forEach((key, value) -> {
            IWebWorker worker;
            try {
                Class<?> referenceToClass = this.getClass().getClassLoader().loadClass((String) value);
                worker = (IWebWorker) referenceToClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | InvocationTargetException
                    | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException("Workers config file has invalid entries.");
            }
            if (workersMap.containsKey(key)) {
                throw new RuntimeException("Workers config file has multiple entries for the same request path.");
            }
            workersMap.put((String) key, worker);
        });
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
        sessionCleanerThread = new SessionCleaner();
        sessionCleanerThread.start();

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
        sessionCleanerThread.kill();
    }

    /**
     * Session map entry for storing sessions.
     */
    private static class SessionMapEntry {
        String sid;
        String host;
        long validUntil;
        Map<String, String> map;

        /**
         * Constructs a new session map entry with given attributes.
         *
         * @param sid  Session id.
         * @param host Host on which session is valid.
         * @throws NullPointerException If given sid or host is {@code null}.
         */
        SessionMapEntry(String sid, String host, long validUntil) {
            this.sid = Objects.requireNonNull(sid);
            this.host = Objects.requireNonNull(host);
            this.validUntil = validUntil;
            map = new ConcurrentHashMap<>();
        }
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
     * Session cleaner thread.
     */
    private class SessionCleaner extends Thread {
        /**
         * Flag that indicates to a thread if it should stop running.
         */
        private volatile boolean shouldTerminate;

        /**
         * Constructs a new daemon thread for cleaning old sessions.
         */
        public SessionCleaner() {
            this.setDaemon(true);
        }

        /**
         * Terminates the thread.
         */
        protected synchronized void kill() {
            shouldTerminate = true;
            this.interrupt();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * 60 * 5); // sleep 5 minutes
                } catch (InterruptedException e) {

                }
                if (shouldTerminate) {
                    break;
                }
                cleanOldSessions();
            }
        }

        /**
         * Removes old sessions from the session map.
         */
        private void cleanOldSessions() {
            Iterator<Map.Entry<String, SessionMapEntry>> it = sessions.entrySet().iterator();
            while (it.hasNext()) {
                SessionMapEntry session = it.next().getValue();
                if (session.validUntil <= System.currentTimeMillis()) {
                    it.remove();
                }
            }
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
        private Map<String, String> permParams = new HashMap<String, String>();
        private List<RequestContext.RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
        private String SID;
        private RequestContext context;

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

            getSessionEntry(header);

//            extract params
            String paramString = fullPath.replaceFirst("^.*?(?:\\?|$)", "");
            parseParameters(paramString);

//            resolve requested path
            String path = fullPath.replaceFirst("\\?.*", "");
            try {
                internalDispatchRequest(path, true);
            } catch (Exception e) {
                e.printStackTrace();
                sendEmptyResponse(500, "Unexpected error occurred.");
            }
        }

        /**
         * Returns a session entry for current request. If no entry is found or existing one is
         * invalid, new entry will be created and returned.
         * This will automatically save session entry to the sessions map and set sid cookie of the request context if needed.
         *
         * @param header Request header.
         * @return Session entry for current request.
         * @throws NullPointerException If given header is {@code null}.
         */
        private SessionMapEntry getSessionEntry(String header) {
            Objects.requireNonNull(header);
            Matcher m = Pattern.compile("\r\nCookie:.*[\\s;]sid=\"([A-Z]+)\"").matcher(header);
            String sidCandidate = null;
            if (m.find()) {
                sidCandidate = m.group(1);
            }
            SessionMapEntry sessionEntry = sessions.get(sidCandidate);

//            session entry is valid -> return it
            if (sessionEntry != null && sessionEntry.validUntil > System.currentTimeMillis()) {
                SID = sessionEntry.sid;
                permParams = sessionEntry.map;
                return sessionEntry;
            }

//            ------------ NEW SESSION ENTRY ------------
//            crete sid
            StringBuilder sb = new StringBuilder();
            sessionRandom.ints(20, 65, 91).forEach(num -> sb.append((char) num));
            sidCandidate = sb.toString();

//            create session entry
            long validUntil = System.currentTimeMillis() + sessionTimeout * 1000;
            sessionEntry = new SessionMapEntry(sidCandidate, host, validUntil);

//            update globals
            SID = sessionEntry.sid;
            sessions.put(sessionEntry.sid, sessionEntry);
            permParams = sessionEntry.map;

//            set sid cookie
            getRequestContext().addRCCookie(new RequestContext.RCCookie("sid", sidCandidate, null, host, "/"));
            return sessionEntry;
        }

        /**
         * Constructs a new request context if not already constructed and returns it.
         *
         * @return Request context.
         */
        private RequestContext getRequestContext() {
            return context = context != null ?
                    context : new RequestContext(ostream, params, permParams, outputCookies, tempParams, this, SID);
        }

        /**
         * Sends empty response and closes client socket.
         *
         * @param code    Response code.
         * @param message Response message.
         * @throws NullPointerException If given message is {@code null}.
         */
        private void sendEmptyResponse(int code, String message) {
            RequestContext rc = new RequestContext(ostream, null, null, null, SID);
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
            int state = 0;
            loop:
            while (true) {
                int b = clientIn.read();
                if (b == -1) {
                    throw new RuntimeException("Header could not be read.");
                }
                bos.write(b);
                switch (state) {
                    case 0:
                        if (b == 13) {
                            state = 1;
                        } else if (b == 10) {
                            state = 4;
                        }
                        break;
                    case 1:
                        if (b == 10) {
                            state = 2;
                        } else {
                            state = 0;
                        }
                        break;
                    case 2:
                        if (b == 13) {
                            state = 3;
                        } else {
                            state = 0;
                        }
                        break;
                    case 3:
                    case 4:
                        if (b == 10) {
                            break loop;
                        } else {
                            state = 0;
                        }
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
            if (urlPath.startsWith("/private/") && directCall) {
                sendEmptyResponse(404, "Path not available.");
                return;
            }

            Path requestedPath;
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

//            get file extension and serve
            String fileExtension = requestedPath.getFileName().toString().replaceFirst("(?:.*\\.|.*)", "");
            try {
                if (urlPath.matches("/ext/[^/]+")) {
                    Class<?> referenceToClass = this.getClass().getClassLoader()
                            .loadClass("hr.fer.zemris.java.webserver.workers." + urlPath.replaceFirst("/ext/", ""));
                    ((IWebWorker) referenceToClass.getConstructor().newInstance()).processRequest(getRequestContext());
                    csocket.close();
                } else if (workersMap.containsKey(urlPath)) {
                    workersMap.get(urlPath).processRequest(getRequestContext());
                    csocket.close();
                } else if (fileExtension.equals("smscr")) {
                    executeSmartScript(Files.readString(requestedPath));
                } else {
                    serveFile(requestedPath);
                }
            } catch (IOException e) {
                sendEmptyResponse(404, "File not found");
            }
        }

        /**
         * Serves a file and closes the connection.
         *
         * @param file File that needs to be served.
         * @throws IOException          If data either could not be read or sent.
         * @throws NullPointerException If given path is {@code null}.
         */
        private void serveFile(Path file) throws IOException {
            Objects.requireNonNull(file);
            String fileExtension = file.getFileName().toString().replaceFirst("(?:.*\\.|.*)", "");
            String mimeType = mimeTypes.getOrDefault(fileExtension, "application/octet-stream");
            RequestContext rc = getRequestContext();
            rc.setMimeType(mimeType);
            rc.write(Files.readAllBytes(file));
            csocket.close();
        }

        /**
         * Executes smart script and closes the client socket.
         *
         * @param script Loaded smart script that needs to be executed.
         * @throws NullPointerException If any of the parameters are {@code null}.
         * @throws IOException          If socket could not be closed.
         */
        private void executeSmartScript(String script) throws IOException {
            Objects.requireNonNull(script);
            try {
                new SmartScriptEngine(
                        new SmartScriptParser(script).getDocumentNode(),
                        getRequestContext()
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
