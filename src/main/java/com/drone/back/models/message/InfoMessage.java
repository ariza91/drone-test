package com.drone.back.models.message;

public class InfoMessage {

    private String message;

    public InfoMessage() {
    }

    public InfoMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
