package com.example.googlemapsdonor.models;

import java.io.Serializable;

public class DonationListModel extends DonationModel {
    private String foodItem;
    private int noOfPersons;

    public DonationListModel() {
        super();
    }

    public DonationListModel(String donorKey, String pickUpLocationKey, String foodKey, String foodItem,int noOfPersons) {
        super(donorKey, pickUpLocationKey, foodKey);
        this.foodItem = foodItem;
        this.noOfPersons = noOfPersons;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public int getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(int noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    @Override
    public String toString() {
        return "DonationModel{" +
                "key='" + getKey() + '\'' +
                ", ngoKey='" + getNgoKey() + '\'' +
                ", donorKey='" + getDonorKey() + '\'' +
                ", pickUpLocationKey='" + getPickUpLocationKey() + '\'' +
                ", ngoLocationKey='" + getNgoLocationKey() + '\'' +
                ", donorLocationKey='" + getDonorLocationKey() + '\'' +
                ", foodKey='" + getFoodKey() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}'+
                "DonationListModel{" +
                "foodItem='" + foodItem + '\'' +
                ", noOfPersons=" + noOfPersons +
                '}';
    }
}
