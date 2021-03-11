package com.drone.back.models.drone;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.drone.back.models.message.BaseMessage;
import com.drone.back.models.message.ReportMessage;
import com.drone.back.rabbitmq.RabbitMQSender;
import com.drone.back.utils.Utils;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import com.drone.back.constants.Commands;
import com.drone.back.constants.Status;

public class Drone implements IDrone {

    private final double MAX_DISTANCE = 350;

    private RabbitMQSender rabbitMQSender;

    private String id;

    private Pair<Double, Double> position = Pair.of(0.0, 0.0);

    private Map<String, Pair<Double, Double>> tubes;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private ArrayBlockingQueue<BaseMessage> messages = new ArrayBlockingQueue<>(10);

    public Drone(String id, Map<String, Pair<Double, Double>> tubes, RabbitMQSender rabbit) {
        this.id = id;
        this.tubes = tubes;
        this.rabbitMQSender = rabbit;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Boolean tubeInRange(Pair<Double, Double> tubePosition) {
        double distance = Utils.calculateDistanceToPoint(tubePosition.getLeft(), tubePosition.getRight(),
                this.position.getLeft(), this.position.getRight());
        return distance <= MAX_DISTANCE;
    }

    @Override
    public void processMessage(BaseMessage msg) {
        if (!id.equals(msg.getDroneId()))
            return;

        switch (msg.getCommand()) {
        case Commands.SHUTDOWN:
            shutdown.set(true);
            break;
        case Commands.MOVE:
            try {
                messages.put(msg);
            } catch (InterruptedException e) {

            }
            break;
        }
    }

    @Override
    public void run() {

        while (!shutdown.get()) {
            BaseMessage msg = messages.poll();
            if (msg == null)
                continue;

            // Move dron to position
            this.position = msg.getPosition();
            // Find next closed tube
            this.tubes.entrySet().stream().filter(tube -> this.tubeInRange(tube.getValue())).findFirst()
                    .ifPresent(tube -> {
                        ReportMessage report = ReportMessage.builder().tube(tube.getKey())
                                .datetime(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                                .status(Status.values()[new Random().nextInt(3)]).build();
                        BaseMessage menssage = BaseMessage.builder().command(Commands.REPORT).droneId(this.id)
                                .report(report).build();

                        this.rabbitMQSender.send(menssage);
                    });
        }

    }

}
