package com.tmnhat.paymentservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.config.RabbitMQConnection;
import com.tmnhat.paymentservice.service.Impl.PaymentServiceImpl;
import com.tmnhat.paymentservice.service.PaymentService;

import java.nio.charset.StandardCharsets;

public class RabbitMQConsumer {

    private static final String QUEUE_NAME = "payment_queue";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Channel channel = RabbitMQConnection.getChannel();
    private final PaymentService paymentService = new PaymentServiceImpl();

    static {
        objectMapper.registerModule(new JavaTimeModule()); // Fix lỗi LocalDateTime
    }

    public void processPaymentQueue() {
        try {
            if (channel == null) {
                System.err.println("❌ Failed to get RabbitMQ channel.");
                return;
            }

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println("Waiting for messages from Booking Service...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received Booking: " + message);

                try {
                    Booking booking = objectMapper.readValue(message, Booking.class);

                    // Xử lý thanh toán
                    paymentService.processPayment(booking);
                    System.out.println("Payment processed for Booking ID: " + booking.getId());
                } catch (Exception e) {
                    System.err.println("Payment processing failed: " + e.getMessage());
                }
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            System.err.println("RabbitMQ Consumer error: " + e.getMessage());
        }
    }
}
