package com.tmnhat.paymentservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.config.RabbitMQConfig;
import com.tmnhat.common.config.RabbitMQConnection;
import com.tmnhat.paymentservice.service.Impl.PaymentServiceImpl;
import com.tmnhat.paymentservice.service.PaymentService;

import java.nio.charset.StandardCharsets;

public class RabbitMQConsumer {
    private final String queueName;
    private final RabbitMQConfig config;
    private final ObjectMapper objectMapper;
    private final Channel channel;

    public RabbitMQConsumer() {
        config = new RabbitMQConfig();
        this.queueName = config.getPaymentQueue();


        // Khởi tạo ObjectMapper và đăng ký module hỗ trợ LocalDateTime
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Lấy channel từ RabbitMQConnection
        channel = RabbitMQConnection.getChannel();
    }


    private final PaymentService paymentService = new PaymentServiceImpl();



    public void processPaymentQueue() {
        try {
            if (channel == null) {
                System.err.println("Failed to get RabbitMQ channel.");
                return;
            }

            channel.queueDeclare(queueName, true, false, false, null);
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

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            System.err.println("RabbitMQ Consumer error: " + e.getMessage());
        }
    }
}
