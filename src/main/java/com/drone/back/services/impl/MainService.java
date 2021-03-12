package com.drone.back.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.drone.back.rabbitmq.RabbitMQSender;
import com.drone.back.services.IMainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drone.back.constants.Commands;
import com.drone.back.dispatcher.DroneDispatcher;
import com.drone.back.models.Position;
import com.drone.back.models.Tube;
import com.drone.back.models.drone.IDrone;
import com.drone.back.models.message.BaseMessage;

@Service
public class MainService implements IMainService {

    @Autowired
    RabbitMQSender rabbitMQSender;

    @Autowired
    List<IDrone> drones;

    private AbstractMap<String, Tube> tubes = new ConcurrentHashMap<>();

    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private List<Future> threads = new ArrayList<>();

    @Override
    public void start() {
        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(ClassLoader.getSystemResourceAsStream("tube.csv")));

            reader.lines().forEach(t -> {
                String[] info = t.split(",");
                tubes.put(info[0],
                        new Tube(info[0], new Position(Double.parseDouble(info[1]), Double.parseDouble(info[2]))));
            });

            drones.forEach(drone -> {
                drone.setDroneConfiguration(tubes, rabbitMQSender);
                threads.add(executor.submit(drone));
                threads.add(executor.submit(new DroneDispatcher(drone, rabbitMQSender)));
            });

        } catch (Exception e) {
            BaseMessage menssage = new BaseMessage(Commands.ERROR, "MainService");
            menssage.createInfo(e.getMessage());
            this.rabbitMQSender.send(menssage);
        }
    }

}
