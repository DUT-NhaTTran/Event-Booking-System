package com.tmnhat.Event.Service.repository;

import com.tmnhat.Event.Service.config.DatabaseConnection;
import com.tmnhat.common.exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public abstract class BaseDAO {
    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    protected int executeUpdate(String query, SQLConsumer<PreparedStatement> consumer) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            consumer.accept(stmt);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected; // Trả về số bản ghi bị ảnh hưởng

        } catch (SQLException e) {
            throw new DatabaseException("Database update error: " + e.getMessage());
        }
    }

    protected <T> T executeQuery(String query, SQLFunction<PreparedStatement, T> function) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            return function.apply(stmt);
        }
    }


    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }
    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}

