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
}
