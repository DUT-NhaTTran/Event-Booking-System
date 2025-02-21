package com.tmnhat.Event.Service.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tmnhat.Event.Service.repository.EventDAO;
import com.tmnhat.common.config.RabbitMQConnection;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RabbitMQConsumer {
    private static final String QUEUE_NAME = "event_update_queue";
    private static final String EXCHANGE_NAME = "payment_exchange";
    private static final String ROUTING_KEY = "payment.event.update";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Channel channel = RabbitMQConnection.getChannel();
    static {
        objectMapper.registerModule(new JavaTimeModule()); // Fix lỗi LocalDateTime
    }

    public void listenForEventUpdates() {
        try {
            if (channel == null) {
                System.err.println("Failed to get RabbitMQ channel.");
                return;
            }

            // Khai báo exchange, queue, binding (chỉ thực hiện 1 lần)
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            System.out.println("Listening for messages on queue: " + QUEUE_NAME);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received event update: " + message);

                try {
                    Map<String, Object> data = objectMapper.readValue(message, Map.class);
                    Long eventId = Long.parseLong(data.get("eventId").toString());
                    int ticketCount = Integer.parseInt(data.get("ticketCount").toString());

                    // Cập nhật số vé trong database
                    new EventDAO().updateEventTickets(eventId, ticketCount);
                    System.out.println("Event ID " + eventId + " updated with " + ticketCount + " tickets.");
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                }
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            System.err.println(" RabbitMQ Consumer error: " + e.getMessage());
        }
    }
}
