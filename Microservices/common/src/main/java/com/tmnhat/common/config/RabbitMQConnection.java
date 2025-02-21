package com.tmnhat.common.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConnection {
    private static Connection connection;
    private static Channel channel;

    private static final String HOST = "localhost";
    private static final int PORT = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";

    private RabbitMQConnection() {

    }

    public static synchronized Connection getConnection() {
        if (connection == null) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(HOST);
                factory.setPort(PORT);
                factory.setUsername(USERNAME);
                factory.setPassword(PASSWORD);
                connection = factory.newConnection();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException("Failed to create RabbitMQ connection: " + e.getMessage());
            }
        }
        return connection;
    }

    public static synchronized Channel getChannel() {
        if (channel == null) {
            try {
                channel = getConnection().createChannel();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create RabbitMQ channel: " + e.getMessage());
            }
        }
        return channel;
    }

    // Đóng kết nối khi không còn sử dụng
    public static synchronized void close() {
        try {
            if (channel != null) {
                channel.close();
                channel = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("❌ Failed to close RabbitMQ connection: " + e.getMessage());
        }
    }
}
