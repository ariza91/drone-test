package com.drone.back.rabbitmq;

import com.drone.back.constants.Commands;
import com.drone.back.models.message.BaseMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "${drone.rabbitmq.queue}")
    public void recievedMessage(BaseMessage msg) {
        if (Commands.REPORT.equals(msg.getCommand()) || Commands.INFO.equals(msg.getCommand())
                || Commands.SHUTDOWN.equals(msg.getCommand()) || Commands.ERROR.equals(msg.getCommand()))
            System.out.println(msg.toString());
    }
}
