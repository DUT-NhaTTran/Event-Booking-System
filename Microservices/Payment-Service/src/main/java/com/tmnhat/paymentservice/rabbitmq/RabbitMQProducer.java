package com.tmnhat.paymentservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.tmnhat.common.config.RabbitMQConnection;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RabbitMQProducer {
    private static final String EXCHANGE_NAME = "payment_exchange";
    private static final Channel channel = RabbitMQConnection.getChannel();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule()); // Fix lỗi LocalDateTime
    }
    public static void sendBookingUpdate(Long bookingId, String status) {
        try {
            if (channel == null) {
                System.err.println("Failed to get RabbitMQ channel.");
                return;
            }

            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            Map<String, Object> message = new HashMap<>();
            message.put("bookingId", bookingId);
            message.put("status", status);
            String messageJson = objectMapper.writeValueAsString(message);

            channel.basicPublish(EXCHANGE_NAME, "payment.booking.update", null, messageJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("📩 Sent booking update: " + messageJson);
        } catch (Exception e) {
            System.err.println("Error sending booking update: " + e.getMessage());
        }
    }

    public static void sendEventUpdate(Long eventId, int ticketCount) {
        try {
            if (channel == null) {
                System.err.println("Failed to get RabbitMQ channel.");
                return;
            }

            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            Map<String, Object> message = new HashMap<>();
            message.put("eventId", eventId);
            message.put("ticketCount", ticketCount);
            String messageJson = objectMapper.writeValueAsString(message);

            channel.basicPublish(EXCHANGE_NAME, "payment.event.update", null, messageJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent event update: " + messageJson);
        } catch (Exception e) {
            System.err.println("Error sending event update: " + e.getMessage());
        }
    }
}
