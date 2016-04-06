package com.cookiescode.trendsmap;

/**
 * Created by Ahmed Ali on 29/03/2016.
 */
public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString(){
        return latitude + ":" + longitude;
    }
}
