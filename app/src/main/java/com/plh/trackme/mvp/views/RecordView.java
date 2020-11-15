package com.plh.trackme.mvp.views;

import com.plh.trackme.mvp.models.Tracking;
import com.plh.trackme.mvp.models.TrackingData;

public interface RecordView extends BaseView {

    void onCreateTrackingSuccess(Tracking tracking);

    void onCreateTrackingDataSuccess(TrackingData trackingData);
}
