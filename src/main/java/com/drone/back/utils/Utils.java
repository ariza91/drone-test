package com.drone.back.utils;

public class Utils {

    public static double calculateDistanceToPoint(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371;
        double distance = Math.acos(
                Math.sin(lat2 * Math.PI / 180.0) * Math.sin(lat1 * Math.PI / 180.0) + Math.cos(lat2 * Math.PI / 180.0)
                        * Math.cos(lat1 * Math.PI / 180.0) * Math.cos((lng1 - lng2) * Math.PI / 180.0))
                * earthRadius;
        return distance;
    }
}
