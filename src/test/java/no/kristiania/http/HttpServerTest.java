package no.kristiania.http;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.TestData;
import no.kristiania.dao.model.Author;
import no.kristiania.http.controllers.AuthorController;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpServerTest {
    private final HttpServer server = new HttpServer(0);

    public HttpServerTest() throws IOException {
    }

    @Test
    void shouldReturn404ForUnknownRequestTarget() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/no-page");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldRespondWithRequestTargetIn404() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/no-page");
        assertEquals("File not found: /no-page", client.getMessageBody());
    }

    @Test
    void shouldRedirectToIndexWhenRequestTargetIsSlash() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/");
        assertTrue(client.getMessageBody().contains("<h1>Library - Main Page</h1>"));
    }

    @Test
    void shouldHandleMoreThanOneRequest() throws IOException {
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/index.html")
                .getStatusCode());
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/index.html")
                .getStatusCode());
    }

    @Test
    void shouldEncodeNorwegianSpecialChar() throws IOException, SQLException {
        DataSource dataSource = TestData.testDataSource();
        AuthorDao authorDao = new AuthorDao(dataSource);
        server.addController("/api/author", new AuthorController(authorDao));

        Author author = new Author();
        author.setFirstName("Øystein");
        author.setLastName("Åsen");


        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/addAuthor", "firstName=Åshild&lastName=Ærestad");

        TestData.cleanDataSource(dataSource);
    }



}
