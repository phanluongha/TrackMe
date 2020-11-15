package com.plh.trackme.mvp.interactors.impl;

import com.plh.trackme.mvp.interactors.RecordInteractor;
import com.plh.trackme.mvp.listeners.RecordListener;
import com.plh.trackme.mvp.models.Tracking;
import com.plh.trackme.mvp.models.TrackingData;

import javax.inject.Inject;

import io.realm.Realm;

public class RecordInteractorImpl extends BaseInteractorImpl implements RecordInteractor {


    @Inject
    public RecordInteractorImpl() {
    }

    @Override
    public void createNewTracking(Realm realm, RecordListener listener) {
        Number currentIdNum = realm.where(Tracking.class).max("id");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        Tracking tracking = new Tracking();
        tracking.setId(nextId);
        realm.executeTransactionAsync(realm1 -> realm1.copyToRealmOrUpdate(tracking),
                () -> listener.onCreateTrackingSuccess(tracking),
                error -> listener.onError());
    }

    @Override
    public void createNewTrackingData(Realm realm, long parentId, double latitude, double longitude, long duration, RecordListener listener) {
        realm.executeTransactionAsync(realm1 -> {
            try {
                Tracking tracking = realm1.where(Tracking.class).equalTo("id", parentId).findFirst();
                if (tracking != null) {
                    TrackingData trackingData = realm1.createObject(TrackingData.class);
                    trackingData.setLatitude(latitude);
                    trackingData.setLongitude(longitude);
                    trackingData.setDuration(duration);
                    tracking.getData().add(trackingData);
                    listener.onCreateTrackingDataSuccess(trackingData);
                }
            } catch (Exception e) {
                listener.onError();
            }

        });
    }
}
