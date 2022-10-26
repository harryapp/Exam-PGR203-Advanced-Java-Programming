package no.kristiania.dao;

import no.kristiania.dao.model.Author;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao extends AbstractDao<Author> {

    public AuthorDao(DataSource dataSource) {
        super(dataSource);
    }

    public void save(Author author) throws SQLException {
        super.save(author, "insert into author(first_name, last_name) values (?, ?)");
    }

    public List<Author> listAll() throws SQLException {
        return super.listAll("select * from author ORDER BY lower(last_name)");
    }

    @Override
    public void delete(long id) throws SQLException {

    }

    public Author retrieve(long id) throws SQLException {
        return super.retrieve(id, "select * from author where id = ?");
    }

    public void update(Author author) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "update author SET(first_name, last_name) = (?, ?) where id = ?"
            )) {

                statement.setString(1, author.getFirstName());
                statement.setString(2, author.getLastName());
                statement.setLong(3, author.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    protected Author mapFromResultSet(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getLong("id"));
        author.setFirstName(rs.getString("first_name"));
        author.setLastName(rs.getString("last_name"));
        return author;
    }

    @Override
    protected void setToStatement(PreparedStatement statement, Author author) throws SQLException {
        statement.setString(1, author.getFirstName());
        statement.setString(2, author.getLastName());

        statement.executeUpdate();
        try (ResultSet rs = statement.getGeneratedKeys()) {
            rs.next();
            author.setId(rs.getLong("id"));
        }
    }

    public List<Author> listByBookId(long bookId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from author INNER JOIN books_authors on (author.id = books_authors.author_id) where book_id = ? ORDER BY lower(author.last_name)")) {
                statement.setLong(1, bookId);
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Author> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(mapFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }

}