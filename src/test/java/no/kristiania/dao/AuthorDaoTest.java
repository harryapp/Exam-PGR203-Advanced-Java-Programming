package no.kristiania.dao;

import no.kristiania.dao.model.Author;
import no.kristiania.dao.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorDaoTest {
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
    void shouldRetrieveSavedAuthor() throws SQLException {
        Author author = TestData.exampleAuthor();
        authorDao.save(author);
        assertThat(authorDao.retrieve(author.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(author);
    }

    @Test
    void ShouldListAllAuthors() throws SQLException {
        Author author1 = TestData.exampleAuthor();
        Author author2 = TestData.exampleAuthor();
        authorDao.save(author1);
        authorDao.save(author2);

        assertThat(authorDao.listAll())
                .extracting(Author::getId)
                .contains(author1.getId(), author2.getId());
    }

    @Test
    void shouldListByBookId() throws SQLException {
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

        assertThat(authorDao.listByBookId(book2.getId()))
                .extracting(Author::getId)
                .contains(book2.getId())
                .doesNotContain(book1.getId());
    }

    @Test
    void ShouldUpdateAuthor() throws SQLException {
        Author author1 = TestData.exampleAuthor();
        authorDao.save(author1);

        Author author2 = TestData.exampleAuthor();
        author2.setId(author1.getId());
        authorDao.update(author2);

        assertThat(authorDao.retrieve(author1.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(author2);
    }

}