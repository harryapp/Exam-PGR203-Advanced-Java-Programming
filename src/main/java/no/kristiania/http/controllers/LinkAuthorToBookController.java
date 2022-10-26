package no.kristiania.http.controllers;

import no.kristiania.dao.BookDao;
import no.kristiania.dao.model.Book;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class LinkAuthorToBookController implements HttpController {
    private final BookDao bookDao;

    public LinkAuthorToBookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {

        Map<String, String> parameters = HttpMessage.parseRequestParameters(request.messageBody);

        bookDao.setAuthor(Long.parseLong(parameters.get("bookId")), Long.parseLong(parameters.get("authorId")));

        HttpMessage response = new HttpMessage("HTTP/1.1 303 See Other", "Author linked", request.fileTarget);
        response.addHeader("Location", "/allBooks.html");
        return response;
    }
}
