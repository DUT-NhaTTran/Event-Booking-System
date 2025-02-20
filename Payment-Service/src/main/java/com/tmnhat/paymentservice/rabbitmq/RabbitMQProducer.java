package com.tmnhat.paymentservice.rabbitmq;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tmnhat.paymentservice.config.RabbitMQConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RabbitMQProducer {
    private static final String EXCHANGE_NAME = "payment_exchange";

    public static void sendBookingUpdate(Long bookingId, String status) {
        try (Channel channel = RabbitMQConnection.getConnection().createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            Map<String, Object> message = new HashMap<>();
            message.put("bookingId", bookingId);
            message.put("status", status);

            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(message);

            // Gửi message đến Booking Service
            channel.basicPublish(EXCHANGE_NAME, "payment.booking.update", null, messageJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent booking update: " + messageJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendEventUpdate(Long eventId, int ticketCount) {
        try (Channel channel = RabbitMQConnection.getConnection().createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            // Chuẩn bị dữ liệu
            Map<String, Object> message = new HashMap<>();
            message.put("eventId", eventId);
            message.put("ticketCount", ticketCount);

            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(message);

            // Gửi message đến Event Service
            channel.basicPublish(EXCHANGE_NAME, "payment.event.update", null, messageJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent event update: " + messageJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
