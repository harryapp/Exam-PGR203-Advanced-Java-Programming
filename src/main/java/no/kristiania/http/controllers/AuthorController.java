package no.kristiania.http.controllers;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.model.Author;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.Map;

public class AuthorController implements HttpController {
    private final AuthorDao authorDao;

    public AuthorController(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        if (request.requestType.equals("GET")) {
            String[] text = request.startLine.split(" ");
            String[] text2 = text[1].split("=");
            long authorId = Long.parseLong(text2[1]);

            Author author = authorDao.retrieve(authorId);
            StringBuilder responseText = new StringBuilder();

            responseText.append("<label>Id: ").append(authorId).append("<br><input type=\"hidden\" name=\"id\" value=\"")
                    .append(author.getId())
                    .append("\"/></label>")
                    .append("<label>First name: <input type=\"text\" name=\"firstName\" value=\"")
                    .append(author.getFirstName())
                    .append("\"/></label><label>Last name: <input type=\"text\" name=\"lastName\" value=\"")
                    .append(author.getLastName())
                    .append("\"/></label>");
            return new HttpMessage("HTTP 200 OK", responseText.toString(), request.fileTarget);

        } else if (request.requestType.equals("POST")) {
            Map<String, String> parameters = HttpMessage.parseRequestParameters(request.messageBody);
            Author author = new Author();
            author.setFirstName(parameters.get("firstName"));
            author.setLastName(parameters.get("lastName"));
            authorDao.save(author);

            HttpMessage response = new HttpMessage("HTTP/1.1 303 See Other", "Author added", request.fileTarget);
            response.addHeader("Location", "/index.html");
            return response;

        } else {
            return new HttpMessage("HTTP/1.1 400 Bad Request", request.requestType);
        }


    }
}
