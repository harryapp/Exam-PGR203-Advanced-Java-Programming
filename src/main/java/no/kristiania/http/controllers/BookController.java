package no.kristiania.http.controllers;

import no.kristiania.dao.BookDao;
import no.kristiania.dao.model.Book;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.Map;

public class BookController implements HttpController {
    private final BookDao bookDao;

    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        HttpMessage response = null;

        if (request.requestType.equals("GET")) {
            String[] text = request.startLine.split(" ");
            String[] text2 = text[1].split("=");
            long bookId = Long.parseLong(text2[1]);

            Book book = bookDao.retrieve(bookId);
            StringBuilder responseText = new StringBuilder();

            responseText.append("<input type=\"hidden\" name=\"id\" value=\"")
                    .append(book.getId())
                    .append("\"/></label>")
                    .append("<label>Title: <input type=\"text\" name=\"title\" value=\"")
                    .append(book.getTitle())
                    .append("\"/></label><label>Description: <input type=\"text\" name=\"description\" value=\"")
                    .append(book.getDescription())
                    .append("\"/></label>");

            response = new HttpMessage("HTTP 200 OK", responseText.toString(), request.fileTarget);

        } else if (request.requestType.equals("POST")) {
            Map<String, String> parameters = HttpMessage.parseRequestParameters(request.messageBody);
            Book book = new Book();
            book.setTitle(parameters.get("title"));
            book.setDescription(parameters.get("description"));
            bookDao.save(book);
            bookDao.setAuthor(book.getId(), Long.parseLong(parameters.get("authorId")));

            response = new HttpMessage("HTTP/1.1 303 See Other", "Book added", request.fileTarget);
            response.addHeader("Location", "/index.html");
        }
        return response;

    }
}
