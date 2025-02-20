package com.tmnhat.paymentservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.*;
import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.paymentservice.config.RabbitMQConnection;
import com.tmnhat.paymentservice.repository.PaymentDAO;
import com.tmnhat.paymentservice.service.Impl.PaymentServiceImpl;
import com.tmnhat.paymentservice.service.PaymentService;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Random;

public class RabbitMQConsumer {

    private static final String QUEUE_NAME = "payment_queue";
    private final PaymentService paymentService = new PaymentServiceImpl();

    public void processPaymentQueue() {
        try (Channel channel = RabbitMQConnection.getConnection().createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println(" Waiting for messages from Booking Service...");

            channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received Booking: " + message);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule()); //Fix lỗi LocalDateTime

                Booking booking = objectMapper.readValue(message, Booking.class);

                try {
                    paymentService.processPayment(booking);
                    System.out.println("Payment processed for Booking ID: " + booking.getId());
                } catch (SQLException e) {
                    System.err.println("Payment processing failed: " + e.getMessage());
                }
            }, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

