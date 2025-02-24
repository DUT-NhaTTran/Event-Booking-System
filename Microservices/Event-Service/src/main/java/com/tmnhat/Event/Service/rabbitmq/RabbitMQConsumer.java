package com.tmnhat.Event.Service.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tmnhat.Event.Service.repository.EventDAO;
import com.tmnhat.common.config.RabbitMQConfig;
import com.tmnhat.common.config.RabbitMQConnection;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RabbitMQConsumer {
    private final String queueName;
    private final String exchangeName;
    private final String routingKey;

    private final RabbitMQConfig config;
    private final ObjectMapper objectMapper;
    private final Channel channel;

    public RabbitMQConsumer() {
        config = new RabbitMQConfig();
        this.queueName = config.getEventUpdateQueue();
        this.exchangeName = config.getEventUpdateExchange();
        this.routingKey = config.getEventUpdateRoutingKey();

        // Khởi tạo ObjectMapper và đăng ký module hỗ trợ LocalDateTime
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Lấy channel từ RabbitMQConnection
        channel = RabbitMQConnection.getChannel();
    }

    public void listenForEventUpdates() {
        try {
            if (channel == null) {
                System.err.println("Failed to get RabbitMQ channel.");
                return;
            }

            // Khai báo exchange, queue và binding sử dụng các giá trị cấu hình đã load
            channel.exchangeDeclare(exchangeName, "direct", true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);

            System.out.println("Listening for messages on queue: " + queueName);

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

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            System.err.println("RabbitMQ Consumer error: " + e.getMessage());
        }
    }
}
