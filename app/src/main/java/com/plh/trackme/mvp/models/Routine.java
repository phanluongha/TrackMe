package com.plh.trackme.mvp.models;

import com.plh.trackme.Util.CommonUtil;

import java.util.ArrayList;

public class Routine {

    public ArrayList<Position> positions = new ArrayList<>();

    private long duration = 0;

    private double distant = 0;

    private double avgSpeed = 0;

    private long trackingId;

    public void addPosition(Position position) {
        if (positions.size() == 0) {
            positions.add(position);
            duration = 0;
            distant = 0;
            avgSpeed = 0;
        } else {
            Position lastPosition = CommonUtil.getLastPosition(positions);
            positions.add(position);
            if (position.getDuration() > 0) {
                duration += position.getDuration();
                double dt = CommonUtil.calculateDistanceInKilometer(position.getLatitude(), position.getLongitude(), lastPosition.getLatitude(), lastPosition.getLongitude());
                distant += dt;
                double speed = dt / CommonUtil.getDurationByHour(position.getDuration());
                if (avgSpeed == 0) {
                    avgSpeed = speed / 2;
                } else {
                    avgSpeed = (avgSpeed + speed) / 2;
                }
            }
        }
    }

    public String getDurationText() {
        return CommonUtil.getDurationText(duration);
    }

    public String getDistantText() {
        return Math.round(distant * 10000.0) / 10000.0 + " km\nDistance";
    }

    public String getAvgSpeedText() {
        return Math.round(avgSpeed * 10000.0) / 10000.0 + " km/h\nSpeed";
    }

    public long getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(long trackingId) {
        this.trackingId = trackingId;
    }
}
