package com.example.googlemapsdonor.models;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class DonationModel implements Serializable {
    private String key;
    private String ngoKey;
    private String donorKey;
    private String pickUpLocationKey;
    private String ngoLocationKey;
    private String donorLocationKey;
    private String foodKey;
    private String status;
    //private HashMap<String,Object> timestampCreated;
    private Object timestampCreated;

    public DonationModel(String donorKey, String pickUpLocationKey, String foodKey) {
        this.donorKey = donorKey;
        this.pickUpLocationKey = pickUpLocationKey;
        this.foodKey = foodKey;
//        HashMap<String,Object> timestamp = new HashMap<>();
//        timestamp.put("timestamp", ServerValue.TIMESTAMP);
//        this.timestampCreated  = timestamp;
      //  this.timestampCreated = ServerValue.TIMESTAMP;
    }
//
    public Object getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Object timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public DonationModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNgoKey() {
        return ngoKey;
    }

    public void setNgoKey(String ngoKey) {
        this.ngoKey = ngoKey;
    }

    public String getDonorKey() {
        return donorKey;
    }

    public void setDonorKey(String donorKey) {
        this.donorKey = donorKey;
    }

    public String getPickUpLocationKey() {
        return pickUpLocationKey;
    }

    public void setPickUpLocationKey(String pickUpLocationKey) {
        this.pickUpLocationKey = pickUpLocationKey;
    }

    public String getNgoLocationKey() {
        return ngoLocationKey;
    }

    public void setNgoLocationKey(String ngoLocationKey) {
        this.ngoLocationKey = ngoLocationKey;
    }

    public String getDonorLocationKey() {
        return donorLocationKey;
    }

    public void setDonorLocationKey(String donorLocationKey) {
        this.donorLocationKey = donorLocationKey;
    }

    public String getFoodKey() {
        return foodKey;
    }

    public void setFoodKey(String foodKey) {
        this.foodKey = foodKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
//
//    public HashMap<String, Object> getTimestampCreated() {
//        return timestampCreated;
//    }
//
//    public void setTimestampCreated(HashMap<String, Object> timestampCreated) {
//        this.timestampCreated = timestampCreated;
//    }
//
//    @Exclude
//    public long getTimeStampCreatedLong(){
//        return (long) timestampCreated.get("timestamp");
//    }

    @Override
    public String toString() {
        return "DonationModel{" +
                "key='" + key + '\'' +
                ", ngoKey='" + ngoKey + '\'' +
                ", donorKey='" + donorKey + '\'' +
                ", pickUpLocationKey='" + pickUpLocationKey + '\'' +
                ", ngoLocationKey='" + ngoLocationKey + '\'' +
                ", donorLocationKey='" + donorLocationKey + '\'' +
                ", foodKey='" + foodKey + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
