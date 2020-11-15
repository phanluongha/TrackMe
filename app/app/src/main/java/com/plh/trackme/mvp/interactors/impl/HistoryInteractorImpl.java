package com.plh.trackme.mvp.interactors.impl;

import com.plh.trackme.mvp.interactors.HistoryInteractor;
import com.plh.trackme.mvp.listeners.HistoryListener;
import com.plh.trackme.mvp.models.Position;
import com.plh.trackme.mvp.models.Routine;
import com.plh.trackme.mvp.models.Tracking;
import com.plh.trackme.mvp.models.TrackingData;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.plh.trackme.Util.Constant.LOAD_MAX_SIZE;

public class HistoryInteractorImpl extends BaseInteractorImpl implements HistoryInteractor {

    @Inject
    public HistoryInteractorImpl() {
    }

    @Override
    public void loadTrackingData(Realm realm, long lastId, HistoryListener listener) {
        realm.executeTransactionAsync(realm1 -> {
            try {
                if (lastId > 0) {
                    RealmResults<Tracking> trackings = realm1.where(Tracking.class)
                            .sort("id", Sort.DESCENDING)
                            .lessThan("id", lastId)
                            .limit(LOAD_MAX_SIZE)
                            .findAll();
                    listener.onLoadTrackingSuccess(convert(trackings));
                } else {
                    RealmResults<Tracking> trackings = realm1.where(Tracking.class)
                            .sort("id", Sort.DESCENDING)
                            .limit(LOAD_MAX_SIZE)
                            .findAll();
                    listener.onLoadTrackingSuccess(convert(trackings));
                }
            } catch (Exception e) {
                listener.onError();
            }

        });
    }


    private ArrayList<Routine> convert(RealmResults<Tracking> trackings) {
        ArrayList<Routine> routines = new ArrayList<>();
        if (trackings != null) {
            for (Tracking tracking : trackings) {
                Routine routine = new Routine();
                routine.setTrackingId(tracking.getId());
                for (TrackingData data : tracking.getData()) {
                    Position position = new Position(data.getLatitude(), data.getLongitude(), data.getDuration());
                    routine.addPosition(position);
                }
                routines.add(routine);
            }
        }
        return routines;
    }

}
