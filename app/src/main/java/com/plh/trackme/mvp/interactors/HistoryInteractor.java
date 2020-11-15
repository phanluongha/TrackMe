package com.plh.trackme.mvp.interactors;

import com.plh.trackme.mvp.listeners.HistoryListener;

import io.realm.Realm;

public interface HistoryInteractor extends BaseInteractor {

    void loadTrackingData(Realm realm, long lastId, HistoryListener listener);
}
