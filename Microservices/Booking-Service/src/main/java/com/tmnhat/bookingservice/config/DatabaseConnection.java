package com.tmnhat.bookingservice.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:8090/booking_dtb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Nhatvn123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    private DatabaseConnection(){

    }

}
