package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao <T>{
    protected DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected T retrieve(long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return mapFromResultSet(rs);
                }
            }
        }
    }

    protected void save(T t, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                setToStatement(statement, t);
            }
        }
    }

    protected abstract T mapFromResultSet(ResultSet rs) throws SQLException;

    protected abstract void setToStatement(PreparedStatement statement, T t) throws SQLException;

    public abstract void save(T t) throws SQLException;

    public abstract List<T> listAll() throws SQLException;

    protected List<T> listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    sql
            )) {

                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<T> list = new ArrayList<>();

                    while (rs.next()) {
                        list.add(mapFromResultSet(rs));
                    }
                    return list;
                }
            }
        }

    }

    public abstract void delete(long id) throws SQLException;

    public abstract void update(T t) throws SQLException;
}
