package com.plh.trackme.mvp.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TrackingData extends RealmObject {


    private double latitude;
    private double longitude;
    private long duration;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
