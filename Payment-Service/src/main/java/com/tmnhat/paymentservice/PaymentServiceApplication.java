package com.tmnhat.paymentservice;

import com.tmnhat.bookingservice.BookingServiceApplication;
import com.tmnhat.bookingservice.rabbitmq.RabbitMQProducer;
import com.tmnhat.paymentservice.model.Payment;
import com.tmnhat.paymentservice.rabbitmq.RabbitMQConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
        System.out.println("Payment Service Application Started");
        new RabbitMQConsumer().processPaymentQueue();

    }
}
