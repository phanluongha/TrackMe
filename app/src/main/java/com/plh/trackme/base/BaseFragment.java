package com.plh.trackme.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.plh.trackme.application.TrackMeApplication;
import com.plh.trackme.dagger.components.AppComponent;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected void injectDependencies(AppComponent appComponent) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), parent, false);
        ButterKnife.bind(this, view);
        injectDependencies(TrackMeApplication.getInstance().getApplicationComponent());
        onCreateView();
        return view;
    }

    protected void onCreateView() {
    }

    protected abstract int getLayoutId();

}
