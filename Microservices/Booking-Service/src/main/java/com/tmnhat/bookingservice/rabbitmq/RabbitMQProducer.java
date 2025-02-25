package com.tmnhat.bookingservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.config.RabbitMQConfig;
import com.tmnhat.common.config.RabbitMQConnection;

import java.nio.charset.StandardCharsets;

public class RabbitMQProducer {
    private static final String queueName;
    private static final String exchangeName;
    private static final String routingKey;

    private static final RabbitMQConfig config;
    private static final ObjectMapper objectMapper;
    private static final Channel channel;

    static{
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        channel= RabbitMQConnection.getChannel();
        config = new RabbitMQConfig();
        exchangeName=config.getPaymentExchange();
        routingKey=config.getPaymentRoutingKey();
        queueName=config.getPaymentQueue();
    }


    public static void sendBookingToPayment(Booking booking) {
        try {
            if (channel == null) {
                System.err.println("Failed to get RabbitMQ channel.");
                return;
            }

            // Đảm bảo exchange, queue và binding đã được khai báo với cấu hình từ properties
            channel.exchangeDeclare(exchangeName, "direct", true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);

            // Chuyển Booking thành JSON
            String bookingJson = objectMapper.writeValueAsString(booking);

            // Gửi message đến Payment Service
            channel.basicPublish(exchangeName, routingKey, null, bookingJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent booking to Payment Service: " + bookingJson);
        } catch (Exception e) {
            System.err.println("Error sending booking to Payment Service: " + e.getMessage());
        }
    }

}
