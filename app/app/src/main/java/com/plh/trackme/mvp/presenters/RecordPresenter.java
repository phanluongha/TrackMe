package com.plh.trackme.mvp.presenters;

import com.plh.trackme.mvp.models.Tracking;

import io.realm.Realm;

public interface RecordPresenter extends BasePresenter {

    void createNewTracking(Realm realm);

    void createNewTrackingData(Realm realm, long parentId, double latitude, double longitude, long duration);

}
