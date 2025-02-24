package com.tmnhat.bookingservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.config.RabbitMQConfig;
import com.tmnhat.common.config.RabbitMQConnection;

import java.nio.charset.StandardCharsets;

public class RabbitMQProducer {
    private final String queueName;
    private final String exchangeName;
    private final String routingKey;

    private final RabbitMQConfig config;
    private final ObjectMapper objectMapper;
    private final Channel channel;

    public RabbitMQProducer(){
        config = new RabbitMQConfig();
        this.queueName = config.getPaymentQueue();
        this.exchangeName = config.getPaymentExchange();
        this.routingKey = config.getPaymentRoutingKey();

        // Khởi tạo ObjectMapper và đăng ký module cho xử lý LocalDateTime
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Lấy channel từ RabbitMQConnection
        channel = RabbitMQConnection.getChannel();
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
