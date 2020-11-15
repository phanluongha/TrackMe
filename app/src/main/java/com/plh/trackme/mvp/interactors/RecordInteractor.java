package com.plh.trackme.mvp.interactors;

import com.plh.trackme.mvp.listeners.RecordListener;

import io.realm.Realm;

public interface RecordInteractor extends BaseInteractor {

    void createNewTracking(Realm realm, RecordListener listener);

    void createNewTrackingData(Realm realm, long parentId, double latitude, double longitude, long duration, RecordListener listener);
}
