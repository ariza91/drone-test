package com.drone.back.models.drone;

import java.util.Map;

import com.drone.back.models.Position;
import com.drone.back.models.Tube;
import com.drone.back.models.message.BaseMessage;
import com.drone.back.rabbitmq.RabbitMQSender;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface IDrone extends Runnable {

    public String getId();

    public void setDroneConfiguration(Map<String, Tube> tubes, RabbitMQSender rabbit);

    public Boolean tubeInRange(Position tubePosition);

    @RabbitListener(queues = "${drone.rabbitmq.queue}")
    public void processMessage(BaseMessage msg);
}
