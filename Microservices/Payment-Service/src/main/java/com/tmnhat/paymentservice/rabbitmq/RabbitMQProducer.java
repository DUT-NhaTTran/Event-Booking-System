package com.tmnhat.paymentservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.tmnhat.common.config.RabbitMQConfig;
import com.tmnhat.common.config.RabbitMQConnection;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RabbitMQProducer {
    private static final RabbitMQConfig config;
    private static final String exchangeName;
    private static final Channel channel;
    private static final ObjectMapper objectMapper;

    static {
        config = new RabbitMQConfig();
        exchangeName = config.getPaymentExchange();
        channel = RabbitMQConnection.getChannel();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static void sendBookingUpdate(Long bookingId, String status) {
        try {
            if (channel == null) {
                System.err.println("Failed to get RabbitMQ channel.");
                return;
            }

            channel.exchangeDeclare(exchangeName, "direct", true);

            Map<String, Object> message = new HashMap<>();
            message.put("bookingId", bookingId);
            message.put("status", status);
            String messageJson = objectMapper.writeValueAsString(message);

            String routingKey = config.getBookingUpdateRoutingKey();
            channel.basicPublish(exchangeName, routingKey, null, messageJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent booking update: " + messageJson);
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

            channel.exchangeDeclare(exchangeName, "direct", true);

            Map<String, Object> message = new HashMap<>();
            message.put("eventId", eventId);
            message.put("ticketCount", ticketCount);
            String messageJson = objectMapper.writeValueAsString(message);

            String routingKey = config.getEventUpdateRoutingKey();
            channel.basicPublish(exchangeName, routingKey, null, messageJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent event update: " + messageJson);
        } catch (Exception e) {
            System.err.println("Error sending event update: " + e.getMessage());
        }
    }
}
