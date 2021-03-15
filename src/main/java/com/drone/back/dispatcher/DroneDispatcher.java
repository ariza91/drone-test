package com.drone.back.dispatcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.drone.back.constants.Commands;
import com.drone.back.models.Position;
import com.drone.back.models.drone.IDrone;
import com.drone.back.models.message.BaseMessage;
import com.drone.back.rabbitmq.RabbitMQSender;

public class DroneDispatcher implements Runnable {

    private RabbitMQSender rabbitMQSender;
    private String droneId;

    public DroneDispatcher(String droneId, RabbitMQSender rabbit) {
        this.droneId = droneId;
        this.rabbitMQSender = rabbit;
    }

    @Override
    public void run() {

        try {

            AtomicBoolean shutdownSent = new AtomicBoolean(false);
            AtomicBoolean finishDay = new AtomicBoolean(false);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(ClassLoader.getSystemResourceAsStream(this.droneId + ".csv")));

            reader.lines().forEach(position -> {

                String[] info = position.replace("\"", "").split(",");

                Position newPos = new Position(Double.parseDouble(info[1]), Double.parseDouble(info[2]));
                String datetime = info[3];
                TemporalAccessor currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(datetime);
                finishDay.set(currentTime.get(ChronoField.HOUR_OF_DAY) == 8
                        && currentTime.get(ChronoField.MINUTE_OF_HOUR) >= 10);

                if (finishDay.get() && !shutdownSent.get()) {
                    shutdownSent.set(true);
                    this.rabbitMQSender.send(new BaseMessage(Commands.SHUTDOWN, this.droneId));
                    return;
                }

                BaseMessage menssage = new BaseMessage(Commands.MOVE, this.droneId);
                menssage.createMovement(newPos, datetime);
                this.rabbitMQSender.send(menssage);

            });

        } catch (Exception e) {
            BaseMessage menssage = new BaseMessage(Commands.ERROR, "DroneDispatcher-" + this.droneId);
            menssage.createInfo(e.getMessage());
            this.rabbitMQSender.send(menssage);
        }
    }

}
