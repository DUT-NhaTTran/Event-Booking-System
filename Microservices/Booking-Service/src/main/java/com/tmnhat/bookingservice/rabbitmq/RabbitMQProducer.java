package com.tmnhat.bookingservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.config.RabbitMQConnection;

import java.nio.charset.StandardCharsets;

public class RabbitMQProducer {
    private static final String EXCHANGE_NAME = "booking_exchange";
    private static final String QUEUE_NAME = "payment_queue";
    private static final String ROUTING_KEY = "booking.payment";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Channel channel = RabbitMQConnection.getChannel();
    static {
        objectMapper.registerModule(new JavaTimeModule()); // Fix lỗi LocalDateTime
    }

    public static void sendBookingToPayment(Booking booking) {
        try {
            if (channel == null) {
                System.err.println(" Failed to get RabbitMQ channel.");
                return;
            }

            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            // Chuyển Booking thành JSON
            String bookingJson = objectMapper.writeValueAsString(booking);

            // Gửi message đến Payment Service
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, bookingJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent booking to Payment Service: " + bookingJson);
        } catch (Exception e) {
            System.err.println("Error sending booking to Payment Service: " + e.getMessage());
        }
    }
}
