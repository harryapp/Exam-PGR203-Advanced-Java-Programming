package no.kristiania.http.controllers;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.model.Author;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;

public class AuthorOptionsController implements HttpController {
    private final AuthorDao authorDao;

    public AuthorOptionsController(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        StringBuilder responseText = new StringBuilder();

        for (Author author : authorDao.listAll()) {
            responseText.append("<option value=").append(author.getId()).append(">").append(author.getLastName()).append(", ").append(author.getFirstName()).append("</option>");
        }

        return new HttpMessage("HTTP 200 OK", responseText.toString(), request.fileTarget);
    }
}
