package no.kristiania.http.controllers;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.BookDao;
import no.kristiania.dao.TestData;
import no.kristiania.dao.model.Author;
import no.kristiania.http.HttpClient;
import no.kristiania.http.HttpServer;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class BookControllerTest {
        private final HttpServer server = new HttpServer(0);

    public BookControllerTest() throws IOException {
    }

    @Test
    void shouldPostAndGetBook() throws IOException, SQLException {
        DataSource dataSource = TestData.testDataSource();
        BookDao bookDao = new BookDao(dataSource);
        AuthorDao authorDao = new AuthorDao(dataSource);

        server.addController("/api/book", new BookController(bookDao));

        Author author = new Author();
        author.setFirstName("Tim");
        author.setLastName("Apple");
        authorDao.save(author);

        HttpClient postClient = new HttpClient("localhost", server.getPort(), "/api/book", "authorId=1&title=bok1&description=description1");
        HttpClient getClient = new HttpClient("localhost", server.getPort(), "/api/book?id=1");
        String expected = "<input type=\"hidden\" name=\"id\" value=\"1\"/></label><label>Title: <input type=\"text\" name=\"title\" value=\"bok1\"/></label><label>Description: <input type=\"text\" name=\"description\" value=\"description1\"/></label>";

        assertEquals(expected, getClient.getMessageBody());

        TestData.cleanDataSource(dataSource);


    }



}