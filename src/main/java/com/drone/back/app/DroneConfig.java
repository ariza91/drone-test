package com.drone.back.app;

import com.drone.back.models.drone.Drone;
import com.drone.back.models.drone.IDrone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroneConfig {

    private final String DRONE1 = "6043";
    private final String DRONE2 = "5937";

    @Bean
    public IDrone drone1() {
        return new Drone(DRONE1);
    }

    @Bean
    public IDrone drone2() {
        return new Drone(DRONE2);
    }
}
