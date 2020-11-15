package com.plh.trackme.application;

import android.app.Application;

import com.plh.trackme.dagger.components.AppComponent;
import com.plh.trackme.dagger.components.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TrackMeApplication extends Application {

    private static TrackMeApplication instance;

    public static TrackMeApplication getInstance() {
        return instance;
    }

    public static void setInstance(TrackMeApplication instance) {
        TrackMeApplication.instance = instance;
    }

    private AppComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);

        Realm.init(getApplicationContext());


        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();

        Realm.setDefaultConfiguration(config);

        applicationComponent = DaggerAppComponent.create();
    }

    public AppComponent getApplicationComponent() {
        return applicationComponent;
    }

}
