package com.drone.back.models.drone;

import java.util.Map;

import com.drone.back.models.message.BaseMessage;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface IDrone extends Runnable {

    public Boolean tubeInRange(Pair<Double, Double> tubePosition);

    // public Pair<String, Pair<Double, Double>> getPosition();

    public void setTubes(Map<String, Pair<Double, Double>> tubes);

    @RabbitListener(queues = "${drone.rabbitmq.queue}")
    public void processMessage(BaseMessage msg);
}
