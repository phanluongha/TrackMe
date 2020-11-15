package com.plh.trackme.dagger.modules;

import com.plh.trackme.dagger.scopes.FragmentScope;
import com.plh.trackme.mvp.interactors.HistoryInteractor;
import com.plh.trackme.mvp.interactors.impl.HistoryInteractorImpl;
import com.plh.trackme.mvp.presenters.HistoryPresenter;
import com.plh.trackme.mvp.presenters.impl.HistoryPresenterImpl;
import com.plh.trackme.mvp.views.HistoryView;

import dagger.Module;
import dagger.Provides;

@Module
public class HistoryModule {

    private HistoryView view;

    public HistoryModule(HistoryView view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    public HistoryView provideView() {
        return view;
    }

    @FragmentScope
    @Provides
    public HistoryInteractor provideInteractor(HistoryInteractorImpl interactor) {
        return interactor;
    }

    @FragmentScope
    @Provides
    public HistoryPresenter providePresenter(HistoryPresenterImpl presenter) {
        return presenter;
    }
}
