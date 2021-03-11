package com.drone.back.models.message;

import com.drone.back.constants.Status;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportMessage {
    private String tube;
    private String datetime;
    private Status status;
}
