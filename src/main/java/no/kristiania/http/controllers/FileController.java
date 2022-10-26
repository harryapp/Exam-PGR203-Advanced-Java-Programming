package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class FileController implements HttpController{
    private String requestFileTarget;

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
        requestFileTarget = request.fileTarget;

        if(requestFileTarget.equals("/"))
            requestFileTarget = "/index.html";

        InputStream fileResource = getClass().getResourceAsStream(requestFileTarget);
        if (fileResource != null) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            fileResource.transferTo(buffer);
            String responseText = buffer.toString();
            return writeOkResponse(responseText);
        }else {
            return notFoundResponse();
        }
    }

    private HttpMessage notFoundResponse(){
        String startLine = "HTTP/1.1 404 Not found";
        String responseMessage = "File not found: " + requestFileTarget;

        return new HttpMessage(startLine, responseMessage, requestFileTarget);
    }

    private HttpMessage writeOkResponse(String responseText) throws IOException {
        String startLine = "HTTP/1.1 200 OK";

        return new HttpMessage(startLine, responseText, requestFileTarget);
    }


}
