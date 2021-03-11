package com.drone.back.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbit;

    @Value("${drone.rabbitmq.exchange}")
    private String exchange;

    @Value("${drone.rabbitmq.routingkey}")
    private String routingkey;

    public void send(String msg) {
        rabbit.convertAndSend(exchange, routingkey, msg);
        System.out.println("Send msg = " + msg);
    }
}
