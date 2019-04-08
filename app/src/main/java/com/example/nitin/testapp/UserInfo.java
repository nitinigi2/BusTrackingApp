package com.example.nitin.testapp;

/**
 * Created by nitin on 3/14/2019.
 */

public class UserInfo {
    private double latitude;
    private double longitude;
    private long time;

    public UserInfo(){

    }
    public UserInfo(double latitude, double longitude, long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTime() {
        return time;
    }
}
