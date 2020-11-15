package com.plh.trackme.Util;

import com.google.android.gms.maps.model.LatLng;
import com.plh.trackme.mvp.models.Position;
import com.plh.trackme.mvp.models.Routine;

import java.util.ArrayList;

public class CommonUtil {

    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    public static Position getFirstPosition(ArrayList<Position> positions) {
        return positions.get(0);
    }

    public static Position getLastPosition(ArrayList<Position> positions) {
        return positions.get(positions.size() - 1);
    }

    public static double calculateDistanceInKilometer(double userLat, double userLng,
                                                      double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH_KM * c;
    }

    public static double getDurationByHour(long duration) {
        return (double) duration / (1000 * 60 * 60);
    }

    public static ArrayList<LatLng> getPolyline(ArrayList<Position> positions) {
        ArrayList<LatLng> result = new ArrayList<>();
        for (Position position : positions) {
            LatLng latLng = new LatLng(position.getLatitude(), position.getLongitude());
            result.add(latLng);
        }
        return result;
    }

    public static void setUpRoutines(ArrayList<Routine> arrRoutineResource, ArrayList<Routine> arrRoutine, boolean refresh) {
        if (refresh) {
            arrRoutineResource = null;
        } else if (arrRoutine != null) {
            if (arrRoutineResource == null) {
                arrRoutineResource = new ArrayList<>();
            }
            arrRoutineResource.addAll(arrRoutine);
        }
    }
}
