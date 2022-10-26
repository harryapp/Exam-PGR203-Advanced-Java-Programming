package no.kristiania.http;

import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.net.Socket;

public class HttpClient {
    private final int statusCode;
    private final HttpMessage httpMessage;
    private String[] statusLine;

    public HttpClient(String host, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(host, port);

        String request = "GET " + requestTarget + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n";
        socket.getOutputStream().write(request.getBytes());

        httpMessage = new HttpMessage(socket);
        statusLine = httpMessage.startLine.split(" ");
        this.statusCode = Integer.parseInt(statusLine[1]);
    }

    public HttpClient(String host, int port, String requestTarget, String messageBody) throws IOException {
        Socket socket = new Socket(host, port);

        String request = "POST " + requestTarget + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Content-Length: " + messageBody.getBytes().length + "\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n" +
                messageBody;
        socket.getOutputStream().write(request.getBytes());

        httpMessage = new HttpMessage(socket);
        statusLine = httpMessage.startLine.split(" ");
        this.statusCode = Integer.parseInt(statusLine[1]);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String[] getStatusLine() {
        return statusLine;
    }

    public String getHeader(String headerName) {
        return httpMessage.headerFields.get(headerName);
    }

    public String getMessageBody() {
        return httpMessage.messageBody;
    }
}
