package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Model of a request context. It also offers all relevant methods for making  a response.
 *
 * @author Jan Capek
 */
public class RequestContext {

    /**
     * Response output stream.
     */
    private OutputStream outputStream;

    /**
     * Request task dispatcher.
     */
    private IDispatcher dispatcher;

    /**
     * Used charset.
     */
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * Character encoding.
     */
    private String encoding = "UTF-8";

    /**
     * Response status code.
     */
    private int statusCode = 200;

    /**
     * Status code text.
     */
    private String statusText = "OK";

    /**
     * Response mime type.
     */
    private String mimeType = "text/html";

    /**
     * Response content length.
     */
    private Long contentLength;

    /**
     * Map with parameters.
     */
    private Map<String, String> parameters;

    /**
     * Map with temporary parameters.
     */
    private Map<String, String> temporaryParameters;

    /**
     * Map with persistent parameters.
     */
    private Map<String, String> persistentParameters;

    /**
     * List of cookies that need to be sent.
     */
    private List<RCCookie> outputCookies;

    /**
     * Flag that indicates if headers are generated.
     */
    private boolean headerGenerated;

    /**
     * Constructs a new request context.
     *
     * @param outputStream         Context output stream; this will be used to respond.
     * @param parameters           Request parameters.
     * @param persistentParameters Persistent request parameters.
     * @param outputCookies        List of output cookies.
     * @throws NullPointerException If given output stream is {@code null}.
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String,
            String> persistentParameters, List<RCCookie> outputCookies) {
        this.outputStream = Objects.requireNonNull(outputStream);
        this.parameters = parameters;
        this.persistentParameters = persistentParameters;
        this.outputCookies = outputCookies;
    }

    /**
     * Constructs a new request context.
     *
     * @param outputStream         Context output stream; this will be used to respond.
     * @param parameters           Request parameters.
     * @param persistentParameters Persistent request parameters.
     * @param outputCookies        List of output cookies.
     * @param temporaryParameters  Map of temporary parameters.
     * @param dispatcher           Request dispatcher.
     * @throws NullPointerException If given output stream is {@code null}.
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters,
                          List<RCCookie> outputCookies, Map<String, String> temporaryParameters, IDispatcher dispatcher) {
        this(outputStream, parameters, persistentParameters, outputCookies);
//        todo: What to do if null?
        this.temporaryParameters = temporaryParameters;
        this.dispatcher = dispatcher;
    }

    /**
     * Writes headers to {@link RequestContext#outputStream}.
     *
     * @throws IOException If headers could not be written.
     */
    private void sendHeaders() throws IOException {
        if (headerGenerated) {
            return;
        }
        String httpStatus = String.format("HTTP/1.1 %d %s\r\n", statusCode, statusText);
        String contentType = "Content-Type: " + mimeType + (mimeType.startsWith("text/") ? "; charset=" + encoding : "") + "\r\n";
        String contentLen = contentLength != null ? String.format("Content-Length: %s", contentLength) : "";
        String cookies = "";
        if (outputCookies != null && outputCookies.size() != 0) {
            cookies = outputCookies.stream()
                    .map(cookie -> String.format("Set-Cookie: %s", cookie))
                    .collect(Collectors.joining("\r\n")) + "\r\n";
        }
        byte[] headerData = (httpStatus + contentType + contentLen + cookies + "\r\n").getBytes(StandardCharsets.ISO_8859_1);
        outputStream.write(headerData);
        headerGenerated = true;
    }

    /**
     * Writes byte array to the response output stream.
     *
     * @param data Data that will be written.
     * @return {@code this}.
     * @throws IOException          If data could not be written.
     * @throws NullPointerException If given array is {@code null}.
     */
    public RequestContext write(byte[] data) throws IOException {
        Objects.requireNonNull(data);
        return write(data, 0, data.length);
    }

    /**
     * Writes byte array to the response output stream starting from given offset and writing given number of bytes.
     *
     * @param data   Data array whose content needs to be sent to output stream.
     * @param offset Offset of the first byte that needs to be written.
     * @param len    Number of bytes that need to be written starting from the offset.
     * @return {@code this}.
     * @throws IOException               If data could not be written.
     * @throws NullPointerException      If given array is {@code null}.
     * @throws IndexOutOfBoundsException If given offset or length is illegal.
     */
    public RequestContext write(byte[] data, int offset, int len) throws IOException {
        Objects.requireNonNull(data);
        sendHeaders();
        outputStream.write(data, offset, len);
        outputStream.flush();
        return this;
    }

    /**
     * Writes given string to the response output stream.
     *
     * @param text Text that needs to be written.
     * @return {@code this}.
     * @throws IOException          If data could not be written.
     * @throws NullPointerException If given text is {@code null}.
     */
    public RequestContext write(String text) throws IOException {
        Objects.requireNonNull(text);
        return write(text.getBytes(charset));
    }

    /**
     * @return Session id.
     */
    public String getSessionID() {
        return "";
    }

    /**
     * Returns names of all parameters.
     *
     * @return Read-only set of names of all parameters. If underlying map is
     * {@code null} or empty, empty set will be returned.
     */
    public Set<String> getParameterNames() {
        Set<String> set = parameters == null ? new HashSet<>(0) : parameters.keySet();
        return Collections.unmodifiableSet(set);
    }

    /**
     * Returns a value of a parameter with given name.
     *
     * @param name Name of the parameter whose value needs to be returned.
     * @return Parameter value or {@code null} if there is no parameter with given name.
     */
    public String getParameter(String name) {
        return parameters == null ? null : parameters.get(name);
    }

    /**
     * Returns names of all persistent parameters.
     *
     * @return Read-only set of names of all persistent parameters. If underlying map is
     * {@code null} or empty, empty set will be returned.
     */
    public Set<String> getPersistentParameterNames() {
        Set<String> set = persistentParameters == null ? new HashSet<>(0) : persistentParameters.keySet();
        return Collections.unmodifiableSet(set);
    }

    /**
     * Returns a value of a persistent parameter with given name.
     *
     * @param name Name of the persistent parameter whose value needs to be returned.
     * @return Parameter value or {@code null} if there is no parameter with given name.
     */
    public String getPersistentParameter(String name) {
        return persistentParameters == null ? null : persistentParameters.get(name);
    }

    /**
     * Stores a value to underlying map of persistent parameters.
     *
     * @param name  Parameter name.
     * @param value Parameter value.
     * @throws NullPointerException If parameter name is {@code null}.
     */
    public void setPersistentParameter(String name, String value) {
        Objects.requireNonNull(name);
        if (persistentParameters == null) {
            persistentParameters = new HashMap<>();
        }
        persistentParameters.put(name, value);
    }

    /**
     * Removes a persistent parameter with given name from underlying map.
     *
     * @param name Name of the parameter that needs to be removed.
     */
    public void removePersistentParameter(String name) {
        if (persistentParameters == null) {
            return;
        }
        persistentParameters.remove(name);
    }

    /**
     * Returns names of all temporary parameters.
     *
     * @return Read-only set of names of all temporary parameters. If underlying map is
     * {@code null} or empty, empty set will be returned.
     */
    public Set<String> getTemporaryParameterNames() {
        Set<String> set = temporaryParameters == null ? new HashSet<>(0) : temporaryParameters.keySet();
        return Collections.unmodifiableSet(set);
    }

    /**
     * Returns a value of a temporary parameter with given name.
     *
     * @param name Name of the temporary parameter whose value needs to be returned.
     * @return Parameter value or {@code null} if there is no parameter with given name.
     */
    public String getTemporaryParameter(String name) {
        return temporaryParameters == null ? null : temporaryParameters.get(name);
    }

    /**
     * Stores a value to underlying map of temporary parameters.
     *
     * @param name  Parameter name.
     * @param value Parameter value.
     * @throws NullPointerException If parameter name is {@code null}.
     */
    public void setTemporaryParameter(String name, String value) {
        Objects.requireNonNull(name);
        if (temporaryParameters == null) {
            temporaryParameters = new HashMap<>();
        }
        temporaryParameters.put(name, value);
    }

    /**
     * Removes a temporary parameter with given name from underlying map.
     *
     * @param name Name of the parameter that needs to be removed.
     */
    public void removeTemporaryParameter(String name) {
        if (temporaryParameters == null) {
            return;
        }
        temporaryParameters.remove(name);
    }

    /**
     * Adds given cookie to the internal list.
     *
     * @param cookie Cookie that needs to be added.
     * @throws NullPointerException  If given cookie is {@code null}.
     * @throws IllegalStateException If headers have already been sent.
     */
    public void addRCCookie(RCCookie cookie) {
        checkHeaders();
        Objects.requireNonNull(cookie);
        if (outputCookies == null) {
            outputCookies = new ArrayList<>();
        }
        outputCookies.add(cookie);
    }

    /**
     * Sets encoding attribute to given encoding.
     *
     * @param encoding New encoding.
     * @throws NullPointerException                         If given encoding is {@code null}.
     * @throws IllegalStateException                        If headers have already been sent.
     * @throws java.nio.charset.IllegalCharsetNameException If encoding name is illegal.
     * @throws java.nio.charset.UnsupportedCharsetException If given encoding is not available on this JVM.
     */
    public void setEncoding(String encoding) {
        checkHeaders();
        Objects.requireNonNull(encoding);
        charset = Charset.forName(encoding);
        this.encoding = encoding;
    }

    /**
     * Sets response status code to given code.
     *
     * @param statusCode New status code.
     * @throws IllegalStateException If headers have already been sent.
     */
    public void setStatusCode(int statusCode) {
        checkHeaders();
        this.statusCode = statusCode;
    }

    /**
     * Sets status text.
     *
     * @param statusText New status text.
     * @throws NullPointerException  If given text is {@code null}.
     * @throws IllegalStateException If headers have already been sent.
     */
    public void setStatusText(String statusText) {
        checkHeaders();
        this.statusText = Objects.requireNonNull(statusText);
    }

    /**
     * Sets mime type.
     *
     * @param mimeType New mime type.
     * @throws NullPointerException  If given string is {@code null}.
     * @throws IllegalStateException If headers have already been sent.
     */
    public void setMimeType(String mimeType) {
        checkHeaders();
        this.mimeType = Objects.requireNonNull(mimeType);
    }

    /**
     * Sets length of response content.
     *
     * @param contentLength Content length.
     * @throws IllegalArgumentException If content length is less than 0.
     * @throws IllegalStateException    If headers have already been sent.
     */
    public void setContentLength(Long contentLength) {
        checkHeaders();
        if (contentLength != null && contentLength < 0) {
            throw new IllegalArgumentException("Content length cannot be less than 0!");
        }
        this.contentLength = contentLength;
    }

    /**
     * Checks if headers are sent and throws an {@link IllegalStateException} if positive.
     *
     * @throws IllegalStateException If headers are sent.
     */
    private void checkHeaders() {
        if (headerGenerated) {
            throw new IllegalStateException("Headers already sent.");
        }
    }

    /**
     * Model of a http cookie.
     */
    public static class RCCookie {

        /**
         * Cookie's name.
         */
        private String name;

        /**
         * Cookie's value.
         */
        private String value;

        /**
         * Specifies hosts to which the cookie will be sent.
         */
        private String domain;

        /**
         * Indicates a URL path that must exist in the requested resource before sending the Cookie header.
         */
        private String path;

        /**
         * Number of seconds until the cookie expires.
         */
        private Integer maxAge;

        /**
         * Constructs a new cookie with given attributes.
         *
         * @param name   Cookie's name.
         * @param value  Cookie's value.
         * @param maxAge Cookie's max age attribute.
         * @param domain Cookie's domain attribute.
         * @param path   Cookie's path attribute.
         * @throws NullPointerException If given name or value is {@code null}.
         */
        public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
            this.name = Objects.requireNonNull(name);
            this.value = Objects.requireNonNull(value);
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
        }

        @Override
        public String toString() {
            String cookie = (
                    String.format("%s=\"%s\"", name, value)
                            + (domain == null ? "" : String.format("; Domain=%s", domain))
                            + (path == null ? "" : String.format("; Path=%s", path))
                            + (maxAge == null ? "" : String.format("; Max-Age=%s", maxAge))
            );
            return cookie;
        }
    }
}
