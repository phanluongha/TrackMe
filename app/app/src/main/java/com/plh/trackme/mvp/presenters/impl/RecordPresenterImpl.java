package com.plh.trackme.mvp.presenters.impl;

import com.plh.trackme.mvp.interactors.RecordInteractor;
import com.plh.trackme.mvp.listeners.RecordListener;
import com.plh.trackme.mvp.models.Tracking;
import com.plh.trackme.mvp.models.TrackingData;
import com.plh.trackme.mvp.presenters.RecordPresenter;
import com.plh.trackme.mvp.views.RecordView;

import javax.inject.Inject;

import io.realm.Realm;

public class RecordPresenterImpl extends BasePresenterImpl implements RecordPresenter, RecordListener {

    private RecordView recordView;
    private RecordInteractor recordInteractor;

    @Inject
    public RecordPresenterImpl(RecordView recordView, RecordInteractor recordInteractor) {
        this.recordView = recordView;
        this.recordInteractor = recordInteractor;
    }

    @Override
    public void createNewTracking(Realm realm) {
        recordInteractor.createNewTracking(realm, this);
    }

    @Override
    public void createNewTrackingData(Realm realm, long parentId, double latitude, double longitude, long duration) {
        recordInteractor.createNewTrackingData(realm, parentId, latitude, longitude, duration, this);
    }

    @Override
    public void cancel() {
        recordView = null;
    }

    @Override
    public void onError() {
        if (recordView != null)
            recordView.onError();
    }

    @Override
    public void onCreateTrackingSuccess(Tracking tracking) {
        if (recordView != null)
            recordView.onCreateTrackingSuccess(tracking);
    }

    @Override
    public void onCreateTrackingDataSuccess(TrackingData trackingData) {
        if (recordView != null)
            recordView.onCreateTrackingDataSuccess(trackingData);
    }
}
