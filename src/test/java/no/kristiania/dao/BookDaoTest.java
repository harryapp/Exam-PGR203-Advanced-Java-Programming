package no.kristiania.dao;

import no.kristiania.dao.model.Author;
import no.kristiania.dao.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class BookDaoTest {
    private DataSource dataSource;
    private AuthorDao authorDao;
    private BookDao bookDao;

    @BeforeEach
    void init() {
        dataSource = TestData.testDataSource();
        authorDao = new AuthorDao(dataSource);
        bookDao = new BookDao(dataSource);
    }

    @AfterEach
    void after() {
        TestData.cleanDataSource(dataSource);
    }

    @Test
    void shouldRetrieveSavedBook() throws SQLException {
        Book book = TestData.exampleBook();
        bookDao.save(book);
        assertThat(bookDao.retrieve(book.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(book);
    }

    @Test
    void setAuthor() {
    }

    @Test
    void listByAuthorId() throws SQLException {
        Author author1 = TestData.exampleAuthor();
        Author author2 = TestData.exampleAuthor();
        authorDao.save(author1);
        authorDao.save(author2);

        Book book1 = TestData.exampleBook();
        Book book2 = TestData.exampleBook();
        bookDao.save(book1);
        bookDao.save(book2);

        bookDao.setAuthor(book1.getId(), author1.getId());
        bookDao.setAuthor(book2.getId(), author2.getId());
        System.out.println(book1.getId());

        assertThat(bookDao.listByAuthorId(author2.getId()))
                .extracting(Book::getId)
                .contains(author2.getId())
                .doesNotContain(book1.getId());
    }

    @Test
    void shouldListAllBooks() throws SQLException {
        Book book1 = TestData.exampleBook();
        Book book2 = TestData.exampleBook();
        bookDao.save(book1);
        bookDao.save(book2);

        assertThat(bookDao.listAll())
                .extracting(Book::getId)
                .contains(book1.getId(), book2.getId());
    }

    @Test
    void shouldUpdateBook() throws SQLException {
        Book book1 = TestData.exampleBook();
        bookDao.save(book1);

        Book book2 = TestData.exampleBook();
        book2.setId(book1.getId());
        bookDao.update(book2);

        assertThat(bookDao.retrieve(book1.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(book2);
    }
}