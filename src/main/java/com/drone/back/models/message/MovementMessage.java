package com.drone.back.models.message;

import org.apache.commons.lang3.tuple.Pair;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovementMessage {
    private Pair<Double, Double> position;
    private String datetime;
}
