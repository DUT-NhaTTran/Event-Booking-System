package com.tmnhat.Event.Service.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:8090/event_dtb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Nhatvn123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Kết nối đến PostgreSQL thành công!");
            } else {
                System.out.println(" Kết nối thất bại!");
            }
        } catch (SQLException e) {
            System.err.println(" Lỗi kết nối PostgreSQL: " + e.getMessage());
        }
    }
}
