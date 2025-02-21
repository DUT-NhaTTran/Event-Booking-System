package com.tmnhat.bookingservice.repository;

import com.tmnhat.bookingservice.config.DatabaseConnection;
import com.tmnhat.common.exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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

    protected long executeUpdateWithGeneratedKeys(String query, SQLConsumer<PreparedStatement> consumer) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            consumer.accept(stmt);
            stmt.executeUpdate();

            // Lấy ID vừa tạo
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return -1; // Trả về -1 nếu không có ID nào được tạo
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

