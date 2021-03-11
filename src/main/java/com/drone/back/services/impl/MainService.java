package com.drone.back.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.drone.back.rabbitmq.RabbitMQSender;
import com.drone.back.services.IMainService;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drone.back.dispatcher.DroneDispatcher;
import com.drone.back.models.drone.Drone;
import com.drone.back.models.drone.IDrone;

@Service
public class MainService implements IMainService {

    @Autowired
    RabbitMQSender rabbitMQSender;

    private AbstractMap<String, Pair<Double, Double>> tubes = new ConcurrentHashMap<>();

    private final String DRONE1 = "6043";
    private final String DRONE2 = "5937";
    private List<IDrone> drones;

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    @Override
    public void start() {
        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(ClassLoader.getSystemResourceAsStream("tube.csv")));

            reader.lines().forEach(tube -> {
                String[] info = tube.split(",");
                tubes.put(info[0], Pair.of(Double.valueOf(info[1]), Double.valueOf(info[2])));
            });

            Drone d1 = new Drone(DRONE1, tubes, rabbitMQSender);
            drones.add(d1);

            Drone d2 = new Drone(DRONE2, tubes, rabbitMQSender);
            drones.add(d2);

            executor.submit(new DroneDispatcher(d1, rabbitMQSender));
            executor.submit(new DroneDispatcher(d2, rabbitMQSender));

        } catch (Exception e) {

        }
    }

}
