package com.drone.back.models;

public class Position {
    private Double longitude;
    private Double latitude;

    public Position() {
    }

    public Position(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Position [latitude=" + latitude + ", longitude=" + longitude + "]";
    }

}
