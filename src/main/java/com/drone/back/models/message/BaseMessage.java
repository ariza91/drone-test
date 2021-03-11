package com.drone.back.models.message;

import com.drone.back.constants.Commands;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseMessage {
    private Commands command;
    private String droneId;
    private MovementMessage movement;
    private ReportMessage report;

    public void setMovement(Double lat, Double lng, String datetime) {
        this.movement = MovementMessage.builder().latitude(lat).longitude(lng).datetime(datetime).build();
    }

    public void setReport(String tube, String datetime, Status status) {
        this.movement = MovementMessage.builder().tube(tube).datetime(datetime).status(status).build();
    }
}
