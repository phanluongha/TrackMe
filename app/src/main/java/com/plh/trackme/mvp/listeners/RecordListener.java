package com.plh.trackme.mvp.listeners;

import com.plh.trackme.mvp.models.Tracking;
import com.plh.trackme.mvp.models.TrackingData;

public interface RecordListener {
    void onError();

    void onCreateTrackingSuccess(Tracking tracking);

    void onCreateTrackingDataSuccess(TrackingData trackingData);
}
