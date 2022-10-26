package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpMessage {
    public String startLine;
    public final Map<String, String> headerFields = new HashMap<>();
    public String messageBody;
    public String requestType;
    public String fileTarget;

    public HttpMessage(Socket socket) throws IOException {
        startLine = HttpMessage.readLine(socket);
        setFileTarget();
        requestType = startLine.split(" ")[0];
        readHeaders(socket);
        if (headerFields.containsKey("Content-Length")) {
            messageBody = HttpMessage.readBytes(socket, getContentLength());
        }
    }

    public HttpMessage(String startLine, String messageBody, String fileTarget) {
        this.startLine = startLine;
        this.fileTarget = fileTarget;
        this.messageBody = messageBody;
    }

    public HttpMessage(String startLine, String messageBody) {
        this.startLine = startLine;
        this.messageBody = messageBody;
    }

    public static Map<String, String> parseRequestParameters(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            int equalsPos = queryParameter.indexOf('=');
            String parameterName = queryParameter.substring(0, equalsPos);
            String parameterValue = queryParameter.substring(equalsPos + 1);
            queryMap.put(parameterName, parameterValue);
        }
        return queryMap;
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public String getHeader(String headerName) {
        return headerFields.get(headerName);
    }

    public void addHeader(String key, String value) {
        headerFields.put(key, value);
    }

    static String readBytes(Socket socket, int contentLength) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            result.append((char) socket.getInputStream().read());
        }
        return URLDecoder.decode(result.toString(), StandardCharsets.UTF_8);
    }

    private void readHeaders(Socket socket) throws IOException {
        String headerLine;
        while (!(headerLine = HttpMessage.readLine(socket)).isBlank()) {
            int colonPos = headerLine.indexOf(':');
            String headerField = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos + 1).trim();
            headerFields.put(headerField, headerValue);
        }
    }

    static String readLine(Socket socket) throws IOException {
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = socket.getInputStream().read()) != '\r') {
            buffer.append((char) c);
        }
        int expectedNewline = socket.getInputStream().read();
        assert expectedNewline == '\n';
        return URLDecoder.decode(buffer.toString(), StandardCharsets.UTF_8);
    }

    public void write(Socket clientSocket) throws IOException {

        String location = "";
        if (headerFields.containsKey("Location"))
            location = "Location: " + headerFields.get("Location") + "\r\n";
        String response = startLine + "\r\n" +
                "Content-Length: " + messageBody.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "Content-Type: " + getContentType() + "\r\n" +
                location +
                "Connection: close\r\n" +
                "\r\n" +
                messageBody;
        clientSocket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
    }

    private void setFileTarget() {
        String[] requestLine = startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String query = null;
        if (questionPos != -1) {
            this.fileTarget = requestTarget.substring(0, questionPos);
            query = requestTarget.substring(questionPos + 1);
        } else {
            this.fileTarget = requestTarget;
        }
    }

    private String getContentType() {
        String contentType = "text/plain; charset=utf-8";
        if (fileTarget.endsWith(".html")) {
            contentType = "text/html; charset=utf-8";
        } else if (fileTarget.endsWith(".css")) {
            contentType = "text/css";
        }
        return contentType;
    }


}
