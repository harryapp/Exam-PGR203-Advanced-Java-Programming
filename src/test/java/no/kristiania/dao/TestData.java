package no.kristiania.dao;

import no.kristiania.dao.model.Author;
import no.kristiania.dao.model.Book;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.util.Random;

public class TestData {
    public static DataSource testDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:library_db;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    public static void cleanDataSource(DataSource dataSource) {
        Flyway.configure().dataSource(dataSource).load().clean();
    }

    private static Random random = new Random();

    public static String pickOne(String... alternates) {
        return alternates[random.nextInt(alternates.length)];
    }

    public static Author exampleAuthor() {
        Author author = new Author();
        author.setFirstName(pickOne("Robert", "Øystein", "Stein", "Hildur", "Hans"));
        author.setLastName(pickOne("Bjørnstad", "Robec", "Åsen", "Beck", "Apple"));
        return author;
    }

    public static Book exampleBook(){
        Book book = new Book();
        book.setTitle(pickOne("Absalom, Absalom!", "A TIME TO KILL", "The House of Mirth", "EAST OF EDEN"));
        book.setDescription(pickOne("This is a description 1", "This is a description 2", "This is a description 3", "This is a description 4"));
        return book;
    }
}
