package no.kristiania;

import no.kristiania.dao.AuthorDao;
import no.kristiania.dao.BookDao;
import no.kristiania.http.HttpServer;
import no.kristiania.http.controllers.*;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);
        DataSource dataSource = createDataSource();

        AuthorDao authorDao = new AuthorDao(dataSource);
        BookDao bookDao = new BookDao(dataSource);


        httpServer.addController("/api/authorOptions", new AuthorOptionsController(authorDao));
        httpServer.addController("/api/authors", new AuthorsController(authorDao));
        httpServer.addController("/api/author", new AuthorController(authorDao));
        httpServer.addController("/api/editAuthor", new EditAuthorController(authorDao));
        httpServer.addController("/api/listBook", new ListBookController(bookDao));
        httpServer.addController("/api/allBooks", new AllBooksController(bookDao));
        httpServer.addController("/api/book", new BookController(bookDao));
        httpServer.addController("/api/editBook", new EditBookController(bookDao));
        httpServer.addController("/api/linkAuthorToBook", new LinkAuthorToBookController(bookDao));

        httpServer.addFileController(new FileController());

        logger.info("Started http://localhost:{}/index.html", httpServer.getPort());
    }

    private static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url", "jdbc:postgresql://localhost:5432/library_db"));
        dataSource.setUser(properties.getProperty("dataSource.username", "library_dbuser"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

}
