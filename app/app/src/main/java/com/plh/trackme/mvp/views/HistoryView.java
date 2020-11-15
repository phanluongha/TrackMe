package com.plh.trackme.mvp.views;

import com.plh.trackme.mvp.models.Routine;

import java.util.ArrayList;

public interface HistoryView extends BaseView {

    void onLoadTrackingSuccess(ArrayList<Routine> routines);

}
