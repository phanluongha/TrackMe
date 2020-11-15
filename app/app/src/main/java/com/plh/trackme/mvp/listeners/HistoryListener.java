package com.plh.trackme.mvp.listeners;

import com.plh.trackme.mvp.models.Routine;
import com.plh.trackme.mvp.models.Tracking;
import com.plh.trackme.mvp.models.TrackingData;

import java.util.ArrayList;

public interface HistoryListener {
    void onError();

    void onLoadTrackingSuccess(ArrayList<Routine> routines);

}
