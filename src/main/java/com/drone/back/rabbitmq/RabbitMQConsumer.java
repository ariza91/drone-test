package com.drone.back.rabbitmq;

import com.drone.back.constants.Commands;
import com.drone.back.models.message.BaseMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "${drone.rabbitmq.queue}")
    public void recievedMessage(BaseMessage msg) {
        if (Commands.MOVE.equals(msg.getCommand()))
            return;

        System.out.println(msg.toString());
    }
}
