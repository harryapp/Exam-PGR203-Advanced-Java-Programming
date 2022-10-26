package no.kristiania.dao;

import no.kristiania.dao.model.Book;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao extends AbstractDao<Book> {

    public BookDao(DataSource dataSource) {
        super(dataSource);
    }

    public void save(Book book) throws SQLException {
        super.save(book, "insert into book(title, description) values (?, ?)");
    }

    public Book retrieve(long id) throws SQLException {
        return super.retrieve(id, "select * from book where id = ?");
    }

    @Override
    protected Book mapFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setDescription(rs.getString("description"));
        return book;
    }

    @Override
    protected void setToStatement(PreparedStatement statement, Book book) throws SQLException {
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getDescription());

        statement.executeUpdate();
        try (ResultSet rs = statement.getGeneratedKeys()) {
            rs.next();
            book.setId(rs.getLong("id"));
        }
    }

    public List<Book> listAll() throws SQLException {
        return super.listAll("select * from book ORDER BY lower(title)");
    }

    @Override
    public void delete(long id) throws SQLException {

    }


    public void update(Book book) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "update book SET(title, description) = (?, ?) where id = (?)"
            )) {
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getDescription());
                statement.setLong(3, book.getId());
                statement.executeUpdate();
            }
        }
    }

    public void setAuthor(long bookId, long authorId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into books_authors(book_id, author_id) values (?, ?)"
            )) {
                statement.setLong(1, bookId);
                statement.setLong(2, authorId);
                statement.executeUpdate();
            }
        }
    }

    public List<Book> listByAuthorId(long authorId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from book INNER JOIN books_authors on (book.id = books_authors.book_id) where author_id = ? ORDER BY lower(book.title)")) {
                statement.setLong(1, authorId);
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Book> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(mapFromResultSet(rs));
                    }
                    return result;
                }

            }
        }
    }


}
