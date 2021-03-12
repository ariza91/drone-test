package com.drone.back.models.drone;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.drone.back.models.Position;
import com.drone.back.models.Tube;
import com.drone.back.models.message.BaseMessage;
import com.drone.back.rabbitmq.RabbitMQSender;
import com.drone.back.utils.Utils;

import com.drone.back.constants.Commands;
import com.drone.back.constants.Status;

public class Drone implements IDrone {

    private final double MAX_DISTANCE = 3.5;

    private RabbitMQSender rabbitMQSender;

    private String id;

    private Position position = new Position(0.0, 0.0);

    private Map<String, Tube> tubes;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private ArrayBlockingQueue<BaseMessage> messages = new ArrayBlockingQueue<>(10);

    public Drone(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setDroneConfiguration(Map<String, Tube> tubes, RabbitMQSender rabbit) {
        this.tubes = tubes;
        this.rabbitMQSender = rabbit;
    }

    @Override
    public Boolean tubeInRange(Position tubePosition) {
        double distance = Utils.calculateDistance(tubePosition.getLatitude(), tubePosition.getLongitude(),
                this.position.getLatitude(), this.position.getLongitude());
        return distance <= MAX_DISTANCE;
    }

    @Override
    public void processMessage(BaseMessage msg) {
        if (!id.equals(msg.getResourceId()))
            return;

        BaseMessage info = new BaseMessage(Commands.INFO, this.id);
        if (Commands.SHUTDOWN.equals(msg.getCommand())) {
            shutdown.set(true);
            info.createInfo("Stopping drone " + this.id);
            this.rabbitMQSender.send(info);

        } else if (Commands.MOVE.equals(msg.getCommand())) {
            try {
                messages.put(msg);
                info.createInfo("New movement received " + msg.getMovement());
                this.rabbitMQSender.send(info);

            } catch (InterruptedException e) {
                BaseMessage menssage = new BaseMessage(Commands.ERROR, this.id);
                menssage.createInfo(e.getMessage());
                this.rabbitMQSender.send(menssage);
            }
        }
    }

    @Override
    public void run() {

        while (!shutdown.get()) {
            BaseMessage msg = messages.poll();

            if (msg == null)
                continue;

            // Move dron to position
            this.position = msg.getMovement().getPosition();

            BaseMessage info = new BaseMessage(Commands.INFO, this.id);
            info.createInfo("Drone moving to " + this.position);
            this.rabbitMQSender.send(info);

            // Find next closed tube
            this.tubes.values().stream()
                    .filter(tube -> Status.NONE.equals(tube.getStatus()) && this.tubeInRange(tube.getPosition()))
                    .findFirst().ifPresent(tube -> {

                        tube.setStatus(Status.values()[new Random().nextInt(3)]);

                        BaseMessage menssage = new BaseMessage(Commands.REPORT, this.id);
                        menssage.createReport(tube.getLocation(), new Date().toString(), tube.getStatus());

                        this.rabbitMQSender.send(menssage);
                    });
        }
    }

}
