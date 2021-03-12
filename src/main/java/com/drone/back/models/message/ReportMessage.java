package com.drone.back.models.message;

import com.drone.back.constants.Status;

public class ReportMessage {

    private String tube;
    private String datetime;
    private Status status;

    public ReportMessage() {
    }

    public ReportMessage(String tube, String datetime, Status status) {
        this.tube = tube;
        this.datetime = datetime;
        this.status = status;
    }

    public String getTube() {
        return tube;
    }

    public void setTube(String tube) {
        this.tube = tube;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "fetched " + tube + " and status changed to " + status + " at " + datetime;
    }
}
