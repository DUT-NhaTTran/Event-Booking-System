package com.tmnhat.paymentservice.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConnection {
    private static Connection connection;

    static {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");

            connection = factory.newConnection();
            System.out.println("Connected to RabbitMQ");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to RabbitMQ");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
