package com.plh.trackme.mvp.models;

public class Position {

    private double latitude;
    private double longitude;

    private long duration;

    public Position(double latitude, double longitude, long duration) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.duration = duration;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getDuration() {
        return duration;
    }


}
