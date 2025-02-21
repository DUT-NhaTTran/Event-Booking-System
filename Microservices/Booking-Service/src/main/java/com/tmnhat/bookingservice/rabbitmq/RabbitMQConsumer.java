package com.tmnhat.bookingservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.*;
import com.tmnhat.bookingservice.repository.BookingDAO;
import com.tmnhat.common.config.RabbitMQConnection;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class RabbitMQConsumer {
    private static final String QUEUE_NAME = "booking_update_queue";
    private static final String EXCHANGE_NAME = "payment_exchange";
    private static final String ROUTING_KEY = "payment.booking.update";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final BookingDAO bookingDAO = new BookingDAO();
    private static final Channel channel = RabbitMQConnection.getChannel();

    static {
        objectMapper.registerModule(new JavaTimeModule()); // Fix lỗi LocalDateTime
    }

    public void startConsumer() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.submit(this::listenForBookingUpdates);
    }

    public void listenForBookingUpdates() {
        try {
            // Sử dụng kênh từ RabbitMQConnection (Singleton)
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            System.out.println("Listening for messages on queue: " + QUEUE_NAME);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received booking update: " + message);

                try {
                    Map<String, Object> data = objectMapper.readValue(message, Map.class);
                    Long bookingId = Long.parseLong(data.get("bookingId").toString());
                    String status = data.get("status").toString();

                    // Cập nhật trạng thái booking trong database
                    bookingDAO.updateBookingStatus(bookingId, status);
                    System.out.println("Booking ID " + bookingId + " updated to " + status);
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                }
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            System.err.println("RabbitMQ Consumer error: " + e.getMessage());
        }
    }
}
