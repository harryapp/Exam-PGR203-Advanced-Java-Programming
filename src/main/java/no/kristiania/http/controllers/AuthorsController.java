package no.kristiania.http.controllers;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.model.Author;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;

public class AuthorsController implements HttpController {
    private final AuthorDao authorDao;

    public AuthorsController(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        StringBuilder responseText = new StringBuilder();
        for (Author author : authorDao.listAll()) {
            responseText.append("<li>")
                    .append(author.getFirstName()).append(" ").append(author.getLastName())
                    .append("<button><a href=\"books.html?authorId=").append(author.getId()).append("\">Books</a></button>")
                    .append("<button><a href=\"editAuthor.html?authorId=").append(author.getId()).append("\">Edit</a></button>")
                    .append("</li>");
        }

        return new HttpMessage("HTTP 200 OK", responseText.toString(), request.fileTarget);
    }
}
