package com.plh.trackme.mvp.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tracking extends RealmObject {

    @PrimaryKey
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private RealmList<TrackingData> data;

    public RealmList<TrackingData> getData() {
        if (data == null)
            data = new RealmList<>();
        return data;
    }
}
