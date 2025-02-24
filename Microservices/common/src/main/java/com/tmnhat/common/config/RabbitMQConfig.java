package com.tmnhat.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RabbitMQConfig {
    private Properties properties = new Properties();

    public RabbitMQConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("rabbitmq.properties")) {
            if (input == null) {
                throw new RuntimeException("Không tìm thấy file rabbitmq.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi load file rabbitmq.properties: " + e.getMessage(), e);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getHost() {
        return properties.getProperty("rabbitmq.host");
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("rabbitmq.port"));
    }

    public String getUsername() {
        return properties.getProperty("rabbitmq.username");
    }

    public String getPassword() {
        return properties.getProperty("rabbitmq.password");
    }

    public String getVirtualHost() {
        return properties.getProperty("rabbitmq.virtual_host");
    }

    public String getBookingUpdateQueue() {
        return properties.getProperty("rabbitmq.booking_update.queue");
    }

    public String getBookingUpdateExchange() {
        return properties.getProperty("rabbitmq.booking_update.exchange");
    }

    public String getBookingUpdateRoutingKey() {
        return properties.getProperty("rabbitmq.booking_update.routing_key");
    }

    public String getPaymentQueue() {
        return properties.getProperty("rabbitmq.payment.queue");
    }

    public String getPaymentExchange() {
        return properties.getProperty("rabbitmq.payment.exchange");
    }

    public String getPaymentRoutingKey() {
        return properties.getProperty("rabbitmq.payment.routing_key");
    }

    public String getEventUpdateQueue() {
        return properties.getProperty("rabbitmq.event_update.queue");
    }

    public String getEventUpdateExchange() {
        return properties.getProperty("rabbitmq.event_update.exchange");
    }

    public String getEventUpdateRoutingKey() {
        return properties.getProperty("rabbitmq.event_update.routing_key");
    }
}
