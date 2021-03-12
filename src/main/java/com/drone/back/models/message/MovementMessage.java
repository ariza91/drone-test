package com.drone.back.models.message;

import com.drone.back.models.Position;

public class MovementMessage {

    private Position position;
    private String datetime;

    public MovementMessage() {
    }

    public MovementMessage(Position position, String datetime) {
        this.position = position;
        this.datetime = datetime;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "to position " + position + " at " + datetime;
    }

}
