package no.kristiania.http.controllers;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.BookDao;
import no.kristiania.dao.model.Book;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.List;

public class ListBookController implements HttpController {
    private final BookDao bookDao;

    public ListBookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String[] text = request.startLine.split(" ");
        String[] text2 = text[1].split("=");
        long authorId = Long.parseLong(text2[1]);

        StringBuilder responseText = new StringBuilder();
        List<Book> bookList = bookDao.listByAuthorId(authorId);
        if(bookList.size() > 0) {
            for (Book book : bookDao.listByAuthorId(authorId)) {
                responseText.append("<li>")
                        .append("<h3>").append(book.getTitle()).append("</h3>")
                        .append("<p>").append(book.getDescription()).append("</p>")

                        .append("</li>");
            }
        }else{
            responseText.append("No Books");
        }

        return new HttpMessage("HTTP 200 OK", responseText.toString(), request.fileTarget);
    }
}
