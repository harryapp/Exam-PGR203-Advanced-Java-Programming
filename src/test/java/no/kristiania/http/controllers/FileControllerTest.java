package no.kristiania.http.controllers;

import no.kristiania.http.HttpClient;
import no.kristiania.http.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileControllerTest {

    private final HttpServer server = new HttpServer(0);

    public FileControllerTest() throws IOException {
    }


    @Test
    void shouldServeFile() throws IOException {
        String fileContent = "A file created at " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/example-file.txt"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.txt");
        assertEquals(fileContent, client.getMessageBody());
        assertEquals("text/plain; charset=utf-8", client.getHeader("Content-Type"));
    }

}