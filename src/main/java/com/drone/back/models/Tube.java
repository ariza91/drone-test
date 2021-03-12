package com.drone.back.models;

import com.drone.back.constants.Status;

public class Tube {
    private String location;
    private Position position;
    private Status status = Status.NONE;

    public Tube(String location, Position position) {
        this.location = location;
        this.position = position;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
