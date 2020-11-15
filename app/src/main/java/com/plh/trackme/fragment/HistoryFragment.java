package com.plh.trackme.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.plh.trackme.R;
import com.plh.trackme.adapter.MapAdapter;
import com.plh.trackme.base.BaseFragment;
import com.plh.trackme.dagger.components.AppComponent;
import com.plh.trackme.dagger.modules.HistoryModule;
import com.plh.trackme.eventBus.BeginRecordEvent;
import com.plh.trackme.mvp.models.Routine;
import com.plh.trackme.mvp.presenters.HistoryPresenter;
import com.plh.trackme.mvp.views.HistoryView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

import static com.plh.trackme.Util.Constant.LOAD_MAX_SIZE;

public class HistoryFragment extends BaseFragment implements HistoryView {

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Inject
    HistoryPresenter historyPresenter;

    Realm mRealm;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MapAdapter mapAdapter;
    private ArrayList<Routine> routines;
    private boolean isLoading = false, isLoadEnd = false;

    @Override
    protected int getLayoutId() {
        return R.layout.frament_history;
    }

    @Override
    protected void injectDependencies(AppComponent appComponent) {
        appComponent.plus(new HistoryModule(this)).inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @OnClick(R.id.record)
    public void beginRecord() {
        EventBus.getDefault().post(new BeginRecordEvent());
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpList(null, true);
        isLoadEnd = false;
        isLoading = false;
        loadTrackingData();
    }

    private void loadTrackingData() {
        long lastTrackingId = 0;
        if (routines != null && routines.size() > 0) {
            lastTrackingId = routines.get(routines.size() - 1).getTrackingId();
        }
        historyPresenter.loadTrackingData(mRealm, lastTrackingId);
    }

    private void setUpList(ArrayList<Routine> arrRoutine, boolean refresh) {
        if (mapAdapter == null) {
            setUpRoutines(arrRoutine, refresh);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mapAdapter = new MapAdapter(routines, getActivity().getApplicationContext());
            mRecyclerView.setAdapter(mapAdapter);
            mRecyclerView.setRecyclerListener(mRecycleListener);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && !isLoading && !isLoadEnd) {
                        isLoading = true;
                        loadTrackingData();
                    }
                }
            });
        } else {
            setUpRoutines(arrRoutine, refresh);
            mapAdapter.updateRoutine(routines);
        }
    }

    private void setUpRoutines(ArrayList<Routine> arrRoutine, boolean refresh) {
        if (refresh) {
            routines = null;
        } else if (arrRoutine != null) {
            if (routines == null) {
                routines = new ArrayList<>();
            }
            routines.addAll(arrRoutine);
        }
    }

    private RecyclerView.RecyclerListener mRecycleListener = holder -> {
        MapAdapter.ViewHolder mapHolder = (MapAdapter.ViewHolder) holder;
        if (mapHolder.map != null) {
            mapHolder.map.clear();
            mapHolder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    };

    @Override
    public void onLoadTrackingSuccess(ArrayList<Routine> routines) {
        isLoading = false;
        if (routines.size() < LOAD_MAX_SIZE) {
            isLoadEnd = true;
        }
        setUpList(routines, false);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        historyPresenter.cancel();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
