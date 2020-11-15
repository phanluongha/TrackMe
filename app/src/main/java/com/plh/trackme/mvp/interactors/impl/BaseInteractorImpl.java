package com.plh.trackme.mvp.interactors.impl;

import com.plh.trackme.mvp.interactors.BaseInteractor;

public class BaseInteractorImpl implements BaseInteractor {

    protected boolean isCanceled;

    @Override
    public void cancel() {
        isCanceled = true;
    }

    @Override
    public void reset() {
        isCanceled = false;
    }
}
