package com.plh.trackme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.plh.trackme.R;
import com.plh.trackme.Util.CommonUtil;
import com.plh.trackme.mvp.models.Position;
import com.plh.trackme.mvp.models.Routine;

import java.util.ArrayList;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {

    private ArrayList<Routine> routines;
    private Context context;

    public MapAdapter(ArrayList<Routine> routines, Context context) {
        super();
        this.routines = routines;
        this.context = context;
    }

    public void updateRoutine(ArrayList<Routine> routines) {
        if (this.routines == null) {
            this.routines = new ArrayList<>();
        } else {
            this.routines.clear();
        }
        if (routines != null) {
            this.routines.addAll(routines);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_tracking_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return routines != null ? routines.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        MapView mapView;
        public GoogleMap map;
        View layout;
        TextView tvDistant;
        TextView tvSpeed;
        TextView tvDuration;

        private ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            mapView = layout.findViewById(R.id.map);
            tvDistant = layout.findViewById(R.id.tv_distant);
            tvSpeed = layout.findViewById(R.id.tv_speed);
            tvDuration = layout.findViewById(R.id.tv_duration);
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            map = googleMap;
            setMapLocation();
        }

        private void setMapLocation() {
            if (map == null) return;

            Routine data = (Routine) mapView.getTag();
            if (data == null || data.positions == null || data.positions.size() == 0) return;

            Position beginPosition = CommonUtil.getFirstPosition(data.positions);
            Position lastPosition = CommonUtil.getLastPosition(data.positions);
            // Add a marker for this item and set the camera
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude()), 15f));
            map.addMarker(new MarkerOptions().position(new LatLng(beginPosition.getLatitude(), beginPosition.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.begin_location)));
            map.addMarker(new MarkerOptions().position(new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location)));
            map.addPolyline(new PolylineOptions().addAll(CommonUtil.getPolyline(data.positions)));
            // Set the map type back to normal.
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        private void bindView(int pos) {
            Routine item = routines.get(pos);
            layout.setTag(this);
            mapView.setTag(item);
            tvDistant.setText(item.getDistantText());
            tvSpeed.setText(item.getAvgSpeedText());
            tvDuration.setText(item.getDurationText());
            setMapLocation();
        }
    }
}
