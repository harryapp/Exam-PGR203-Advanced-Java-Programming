package no.kristiania.http.controllers;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.model.Author;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.Map;

public class EditAuthorController implements HttpController {
    private final AuthorDao authorDao;

    public EditAuthorController(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> parameters = HttpMessage.parseRequestParameters(request.messageBody);
        Author author = new Author();
        author.setId(Long.parseLong(parameters.get("id")));
        author.setFirstName(parameters.get("firstName"));
        author.setLastName(parameters.get("lastName"));
        authorDao.update(author);

        HttpMessage response = new HttpMessage("HTTP/1.1 303 See Other", "Author updated", request.fileTarget);
        response.addHeader("Location", "/showLibrary.html");
        return response;
    }
}
