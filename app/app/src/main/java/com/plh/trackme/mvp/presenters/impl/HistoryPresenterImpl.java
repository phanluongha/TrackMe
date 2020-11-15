package com.plh.trackme.mvp.presenters.impl;

import com.plh.trackme.mvp.interactors.HistoryInteractor;
import com.plh.trackme.mvp.listeners.HistoryListener;
import com.plh.trackme.mvp.models.Routine;
import com.plh.trackme.mvp.presenters.HistoryPresenter;
import com.plh.trackme.mvp.views.HistoryView;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;

public class HistoryPresenterImpl extends BasePresenterImpl implements HistoryPresenter, HistoryListener {

    private HistoryView historyView;
    private HistoryInteractor historyInteractor;

    @Inject
    public HistoryPresenterImpl(HistoryView historyView, HistoryInteractor historyInteractor) {
        this.historyView = historyView;
        this.historyInteractor = historyInteractor;
    }

    @Override
    public void loadTrackingData(Realm realm, long lastId) {
        historyInteractor.loadTrackingData(realm, lastId, this);
    }

    @Override
    public void cancel() {
        historyView = null;
    }

    @Override
    public void onError() {
        if (historyView != null)
            historyView.onError();
    }

    @Override
    public void onLoadTrackingSuccess(ArrayList<Routine> routines) {
        if (historyView != null)
            historyView.onLoadTrackingSuccess(routines);
    }
}
