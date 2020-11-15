package com.plh.trackme.mvp.presenters;

import io.realm.Realm;

public interface HistoryPresenter extends BasePresenter {

    void loadTrackingData(Realm realm, long lastId);

}
