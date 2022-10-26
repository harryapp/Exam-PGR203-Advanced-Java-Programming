package no.kristiania.http.controllers;

import no.kristiania.dao.BookDao;
import no.kristiania.dao.model.Book;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;

public class AllBooksController implements HttpController {
    private final BookDao bookDao;

    public AllBooksController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        StringBuilder responseText = new StringBuilder();

        for (Book book : bookDao.listAll()) {
            responseText.append("<li>")
                    .append("<h3>").append(book.getTitle()).append("</h3>")
                    .append("<p>").append(book.getDescription()).append("</p>")
                    .append("<button><a href=\"editBook.html?bookId=").append(book.getId()).append("\">Edit</a></button>")
                    .append("<button><a href=\"linkAuthorToBook.html?bookId=").append(book.getId()).append("\">Link Author</a></button>")
                    .append("</li>");
        }

        return new HttpMessage("HTTP 200 OK", responseText.toString(), request.fileTarget);
    }
}
