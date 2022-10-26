package no.kristiania.http.controllers;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.TestData;
import no.kristiania.http.HttpClient;
import no.kristiania.http.HttpServer;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AuthorControllerTest {
    private final HttpServer server = new HttpServer(0);

    public AuthorControllerTest() throws IOException {
    }

    @Test
    void shouldPostAndGetAuthor() throws IOException {
        DataSource dataSource = TestData.testDataSource();
        AuthorDao authorDao = new AuthorDao(dataSource);

        server.addController("/api/author", new AuthorController(authorDao));

        HttpClient postClient = new HttpClient("localhost", server.getPort(), "/api/author", "firstName=Robert&lastName=Hansen");
        HttpClient getClient = new HttpClient("localhost", server.getPort(), "/api/author?id=1");
        String expected = "<label>Id: 1<br><input type=\"hidden\" name=\"id\" value=\"1\"/></label><label>First name: <input type=\"text\" name=\"firstName\" value=\"Robert\"/></label><label>Last name: <input type=\"text\" name=\"lastName\" value=\"Hansen\"/></label>";

        assertEquals(expected, getClient.getMessageBody());

        TestData.cleanDataSource(dataSource);
    }


}