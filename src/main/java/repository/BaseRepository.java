package repository;

import java.sql.Connection;
import java.sql.SQLException;

import database.ConnectionFactory;

public abstract class BaseRepository {

    protected Connection getConnection() throws SQLException {
        return ConnectionFactory.getConnection();
    }

    protected void handleSQLException(SQLException e, String operation) {
        System.err.println("Database error in " + operation + ": " + e.getMessage());
        e.printStackTrace();
    }
}
