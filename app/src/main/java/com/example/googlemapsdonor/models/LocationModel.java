package com.example.googlemapsdonor.models;

public class LocationModel {
    private Double longitude;
    private Double latitude;
    private String time;
    private String locationKey;
    private boolean trackingStatus;


    public LocationModel(Double longitude, Double latitude, String time) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
    }


    public LocationModel(){

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }

    public Double getLongitute() {
        return longitude;
    }

    public void setLongitute(Double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isStatus() {
        return trackingStatus;
    }

    public void setStatus(boolean status) {
        this.trackingStatus = status;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", time='" + time + '\'' +
                ", locationKey='" + locationKey + '\'' +
                ", trackingStatus=" + trackingStatus +
                '}';
    }
}
