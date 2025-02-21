package com.tmnhat.Event.Service;

import com.tmnhat.Event.Service.rabbitmq.RabbitMQConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
		new RabbitMQConsumer().listenForEventUpdates();
	}

}
