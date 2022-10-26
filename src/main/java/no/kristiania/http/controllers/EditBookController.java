package no.kristiania.http.controllers;

import no.kristiania.dao.BookDao;
import no.kristiania.dao.model.Author;
import no.kristiania.dao.model.Book;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.Map;

public class EditBookController implements HttpController {
    private final BookDao bookDao;

    public EditBookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> parameters = HttpMessage.parseRequestParameters(request.messageBody);
        Book book = new Book();
        book.setId(Long.parseLong(parameters.get("id")));
        book.setTitle(parameters.get("title"));
        book.setDescription(parameters.get("description"));
        bookDao.update(book);

        HttpMessage response = new HttpMessage("HTTP/1.1 303 See Other", "Book updated", request.fileTarget);
        response.addHeader("Location", "/allBooks.html");
        return response;

    }

}
