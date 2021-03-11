package com.drone.back.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "${drone.rabbitmq.queue}")
    public void recievedMessage(String msg) {
        System.out.println("Recieved Message From RabbitMQ: " + msg);
    }
}
