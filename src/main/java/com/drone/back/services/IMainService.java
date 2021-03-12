package com.drone.back.services;

import com.drone.back.models.message.BaseMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface IMainService {

    void start();

    @RabbitListener(queues = "${drone.rabbitmq.queue}")
    void shutdown(BaseMessage msg);
}
