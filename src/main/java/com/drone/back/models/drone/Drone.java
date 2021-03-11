package com.drone.back.models.drone;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.drone.back.models.message.BaseMessage;
import com.drone.back.utils.Utils;

import org.apache.commons.lang3.tuple.Pair;
import com.drone.back.constants.Commands;

public class Drone implements IDrone {

    private final double MAX_DISTANCE = 350;

    private String id;

    private Pair<Double, Double> position = Pair.of(0.0, 0.0);

    private Map<String, Pair<Double, Double>> tubes;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private ArrayBlockingQueue<BaseMessage> messages = new ArrayBlockingQueue<>(10);

    public Drone(String id) {
        this.id = id;
    }

    @Override
    public void run() {

        while (!shutdown.get()) {
            BaseMessage msg = messages.poll();
            // Move dron to position
            this.position = msg.getPosition();
            // Find next closed tube
            this.tubes.entrySet().stream().filter(tube -> this.tubeInRange(tube.getValue())).findFirst()
                    .ifPresent(tube -> {
                        
                        this.send(ReportMessage
                                        .builder()
                                        .droneId(droneId)
                                        .time(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(ZonedDateTime.now()))
                                        .speed("60mph")
                                        .tubeName(tubeData.getKey())
                                        .status(ReportStatus.values()[new Random().nextInt(3)])
                                        .build())
                    });
        }

    }

    @Override
    public Boolean tubeInRange(Pair<Double, Double> tubePosition) {
        double distance = Utils.calculateDistanceToPoint(tubePosition.getLeft(), tubePosition.getRight(),
                this.position.getLeft(), this.position.getRight());
        return distance <= MAX_DISTANCE;
    }

    @Override
    public void setTubes(Map<String, Pair<Double, Double>> tubes) {
        this.tubes = tubes;
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

}
