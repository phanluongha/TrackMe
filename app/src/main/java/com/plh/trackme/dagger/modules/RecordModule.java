package com.plh.trackme.dagger.modules;

import com.plh.trackme.dagger.scopes.FragmentScope;
import com.plh.trackme.mvp.interactors.RecordInteractor;
import com.plh.trackme.mvp.interactors.impl.RecordInteractorImpl;
import com.plh.trackme.mvp.presenters.RecordPresenter;
import com.plh.trackme.mvp.presenters.impl.RecordPresenterImpl;
import com.plh.trackme.mvp.views.RecordView;

import dagger.Module;
import dagger.Provides;

@Module
public class RecordModule {

    private RecordView view;

    public RecordModule(RecordView view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    public RecordView provideView() {
        return view;
    }

    @FragmentScope
    @Provides
    public RecordInteractor provideInteractor(RecordInteractorImpl interactor) {
        return interactor;
    }

    @FragmentScope
    @Provides
    public RecordPresenter providePresenter(RecordPresenterImpl presenter) {
        return presenter;
    }
}
