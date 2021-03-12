package com.drone.back.models.message;

import com.drone.back.constants.Commands;
import com.drone.back.constants.Status;
import com.drone.back.models.Position;

public class BaseMessage {
    private Commands command;
    private String resourceId;
    private MovementMessage movement;
    private ReportMessage report;
    private InfoMessage info;

    public BaseMessage() {
    }

    public BaseMessage(Commands command, String resourceId) {
        this.command = command;
        this.resourceId = resourceId;
    }

    public void createMovement(Position position, String datetime) {
        this.movement = new MovementMessage(position, datetime);
    }

    public void createReport(String tube, String datetime, Status status) {
        this.report = new ReportMessage(tube, datetime, status);
    }

    public void createInfo(String message) {
        this.info = new InfoMessage(message);
    }

    public Commands getCommand() {
        return command;
    }

    public void setCommand(Commands command) {
        this.command = command;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public MovementMessage getMovement() {
        return movement;
    }

    public void setMovement(MovementMessage movement) {
        this.movement = movement;
    }

    public ReportMessage getReport() {
        return report;
    }

    public void setReport(ReportMessage report) {
        this.report = report;
    }

    public InfoMessage getInfo() {
        return info;
    }

    public void setInfo(InfoMessage info) {
        this.info = info;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(command + "[" + resourceId + "] >>> ");
        if (this.info != null)
            stringBuilder.append(this.info.toString());
        if (this.movement != null)
            stringBuilder.append(this.movement.toString());
        if (this.report != null)
            stringBuilder.append(this.report.toString());
        return stringBuilder.toString();
    }

}
