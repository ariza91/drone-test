package com.drone.back.models.drone;

import com.drone.back.models.message.BaseMessage;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface IDrone extends Runnable {

    public String getId();

    public Boolean tubeInRange(Pair<Double, Double> tubePosition);

    @RabbitListener(queues = "${drone.rabbitmq.queue}")
    public void processMessage(BaseMessage msg);
}
