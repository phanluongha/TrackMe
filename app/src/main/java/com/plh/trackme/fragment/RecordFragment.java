package com.plh.trackme.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.plh.trackme.R;
import com.plh.trackme.Util.CommonUtil;
import com.plh.trackme.Util.Constant;
import com.plh.trackme.base.BaseFragment;
import com.plh.trackme.dagger.components.AppComponent;
import com.plh.trackme.dagger.modules.RecordModule;
import com.plh.trackme.eventBus.StopRecordEvent;
import com.plh.trackme.mvp.models.Position;
import com.plh.trackme.mvp.models.Routine;
import com.plh.trackme.mvp.models.Tracking;
import com.plh.trackme.mvp.models.TrackingData;
import com.plh.trackme.mvp.presenters.RecordPresenter;
import com.plh.trackme.mvp.views.RecordView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

public class RecordFragment extends BaseFragment implements RecordView, OnMapReadyCallback {

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @BindView(R.id.tv_distant)
    TextView tvDistant;

    @BindView(R.id.tv_speed)
    TextView tvSpeed;

    @BindView(R.id.tv_duration)
    TextView tvDuration;

    @BindView(R.id.btn_record)
    Button btnRecord;

    @BindView(R.id.btn_stop)
    Button btnStop;

    @BindView(R.id.btn_resume)
    Button btnResume;

    @BindView(R.id.btn_done)
    Button btnDone;

    @Inject
    RecordPresenter recordPresenter;

    Realm mRealm;

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Handler handler = new Handler();
    private Routine routine = new Routine();
    private boolean setCurrentZoom = false;
    private Polyline polyline;
    private Marker beginPoint, currentPoint;
    private int state = Constant.STATE_INIT;
    private Long currentTrackingId, currentTime;

    @Override
    protected int getLayoutId() {
        return R.layout.frament_record;
    }

    @Override
    protected void injectDependencies(AppComponent appComponent) {
        appComponent.plus(new RecordModule(this)).inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onCreateView() {
        // Google map view
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
    }


    @OnClick(R.id.btn_record)
    public void record() {
        state = Constant.STATE_RECORDING;
        showButtonAction();
        checkPermission();
    }

    @OnClick(R.id.btn_resume)
    public void resume() {
        state = Constant.STATE_RECORDING;
        showButtonAction();
        checkPermission();
    }

    @OnClick(R.id.btn_stop)
    public void stop() {
        state = Constant.STATE_STOP;
        showButtonAction();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @OnClick(R.id.btn_done)
    public void done() {
        EventBus.getDefault().post(new StopRecordEvent());
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.REQUEST_LOCATION);
        } else {
            if (currentTrackingId == null) {
                recordPresenter.createNewTracking(mRealm);
            } else {
                trackingLocation();
            }
        }
    }

    private void trackingLocation() {
        currentTime = null;
        getDeviceLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.REQUEST_LOCATION) {
            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && permissions[1].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                trackingLocation();
            } else {
                Toast.makeText(getContext(), "Please enable location permission", Toast.LENGTH_LONG).show();
                state = Constant.STATE_INIT;
                showButtonAction();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    private Runnable getLocationRunnable = () -> getDeviceLocation();

    private void getDeviceLocation() {
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Location lastKnownLocation = task.getResult();
                    if (lastKnownLocation != null && mGoogleMap != null) {
                        long duration = 0;
                        if (currentTime != null) {
                            duration = System.currentTimeMillis() - currentTime;
                        }
                        Position currentPosition = addPosition(lastKnownLocation, duration);
                        updateMarkers();
                        updateRoutine(currentPosition);
                        focusCurrentPosition(currentPosition);
                        recordPresenter.createNewTrackingData(mRealm, currentTrackingId, currentPosition.getLatitude(), currentPosition.getLongitude(), duration);
                    }
                    currentTime = System.currentTimeMillis();
                    handler.postDelayed(getLocationRunnable, Constant.TIME_UPDATE_LOCATION);
                }
            });
        } catch (SecurityException e) {
        }
    }

    private Position addPosition(Location location, long duration) {
        Position currentPosition = new Position(location.getLatitude(), location.getLongitude(), duration);
        routine.addPosition(currentPosition);
        tvDistant.setText(routine.getDistantText());
        tvDuration.setText(routine.getDurationText());
        tvSpeed.setText(routine.getAvgSpeedText());
        return currentPosition;
    }

    private void updateMarkers() {
        if (beginPoint == null) {
            Position beginPosition = CommonUtil.getFirstPosition(routine.positions);
            MarkerOptions beginPointOption = new MarkerOptions().position(new LatLng(beginPosition.getLatitude(), beginPosition.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.begin_location));
            beginPoint = mGoogleMap.addMarker(beginPointOption);
        }
        Position lastPosition = CommonUtil.getLastPosition(routine.positions);
        if (currentPoint == null) {
            MarkerOptions currentPointOption = new MarkerOptions().position(new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
            currentPoint = mGoogleMap.addMarker(currentPointOption);
        } else {
            currentPoint.setPosition(new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude()));
        }
    }

    private void updateRoutine(Position currentPosition) {
        if (polyline == null) {
            polyline = mGoogleMap.addPolyline(new PolylineOptions().add(new LatLng(currentPosition.getLatitude(), currentPosition.getLongitude())));
        } else {
            List<LatLng> points = polyline.getPoints();
            points.add(new LatLng(currentPosition.getLatitude(), currentPosition.getLongitude()));
            polyline.setPoints(points);
        }
    }

    private void focusCurrentPosition(Position currentPosition) {
        if (!setCurrentZoom) {
            setCurrentZoom = true;
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentPosition.getLatitude(),
                            currentPosition.getLongitude()), 15));
        } else {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(currentPosition.getLatitude(),
                            currentPosition.getLongitude())));
        }
    }

    private void showButtonAction() {
        btnRecord.setVisibility(state == Constant.STATE_INIT ? View.VISIBLE : View.GONE);
        btnResume.setVisibility(state == Constant.STATE_STOP ? View.VISIBLE : View.GONE);
        btnStop.setVisibility(state == Constant.STATE_RECORDING ? View.VISIBLE : View.GONE);
        btnDone.setVisibility(state == Constant.STATE_STOP ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateTrackingSuccess(Tracking tracking) {
        currentTrackingId = tracking.getId();
        trackingLocation();
    }

    @Override
    public void onCreateTrackingDataSuccess(TrackingData trackingData) {
        Log.e("trackingData", trackingData.getLatitude() + "-" + trackingData.getLongitude());
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "Create data error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recordPresenter.cancel();
        if (mRealm != null) {
            mRealm.close();
        }
        if (mGoogleMap != null)
            mGoogleMap = null;
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }
}
