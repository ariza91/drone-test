package com.drone.back.dispatcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import com.drone.back.constants.Commands;
import com.drone.back.models.drone.IDrone;
import com.drone.back.models.message.BaseMessage;
import com.drone.back.models.message.MovementMessage;
import com.drone.back.rabbitmq.RabbitMQSender;

import org.apache.commons.lang3.tuple.Pair;

public class DroneDispatcher implements Runnable {

    private RabbitMQSender rabbitMQSender;
    private IDrone drone;

    public DroneDispatcher(IDrone d, RabbitMQSender rabbit) {
        this.drone = d;
        this.rabbitMQSender = rabbit;
    }

    @Override
    public void run() {
        try {
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(ClassLoader.getSystemResourceAsStream(this.drone.getId() + ".csv")));

            reader.lines().forEach(position -> {
                String[] info = position.split(",");

                Pair<Double, Double> newPos = Pair.of(Double.parseDouble(info[1]), Double.parseDouble(info[2]));
                String datetime = info[3];
                TemporalAccessor currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(datetime);
                Boolean finishDay = currentTime.get(ChronoField.HOUR_OF_DAY) == 8 && currentTime.get(ChronoField.MINUTE_OF_HOUR) > 11;
                
                if (finishDay) {
                    BaseMessage menssage = BaseMessage.builder()
                        .command(Commands.SHUTDOWN)
                        .droneId(this.drone.getId())
                        .build();
                    this.rabbitMQSender.send(menssage);
                    return;
                }

                MovementMessage movement = MovementMessage.builder()
                        .position(newPos)
                        .datetime(datetime)
                        .build();
                BaseMessage menssage = BaseMessage.builder()
                        .command(Commands.MOVE)
                        .droneId(this.drone.getId())
                        .movement(movement)
                        .build();

                this.rabbitMQSender.send(menssage);

            });
        } catch (e) {
            // error
        }
    }

}
