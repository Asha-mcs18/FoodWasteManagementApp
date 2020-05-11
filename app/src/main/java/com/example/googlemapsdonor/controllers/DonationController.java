package com.example.googlemapsdonor.controllers;

import android.location.Location;
import android.util.Log;

import com.example.googlemapsdonor.firebasehandler.FBDonationHandler;
import com.example.googlemapsdonor.firebasehandler.FBFoodHandler;
import com.example.googlemapsdonor.firebasehandler.FBLocationHandler;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationModel;
import com.example.googlemapsdonor.models.FoodModel;
import com.example.googlemapsdonor.models.LocationModel;
import com.example.googlemapsdonor.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DonationController {
    private FBDonationHandler donationHandler;
    private FBFoodHandler foodHandler;
    private FBLocationHandler locationHandler;
    private FirebaseAuth firebaseAuth;
    private String mdonorKey;
    private String mfoodKey;
    private String mlocationKey;

    public DonationController(){
        donationHandler = new FBDonationHandler();
        foodHandler = new FBFoodHandler();
        locationHandler = new FBLocationHandler();
        firebaseAuth= FirebaseAuth.getInstance();
    }

    public void createNewDonation(FoodModel food, final LocationModel pickUpLocation, final DataStatus appDataStatus){
        mdonorKey=null;
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            mdonorKey = currentUser.getUid();
        }
        Log.d("Donation Controller 1","Donor key,Food Ky, location key "+mdonorKey+" "+ mfoodKey +" " + mlocationKey);
        if(mdonorKey!=null) {
            foodHandler.addFood(food, new DataStatus() {
                @Override
                public void dataCreated(String foodKey) {
                    super.dataCreated(foodKey);
                    mfoodKey = foodKey;
                    Log.d("Donation Controller 2","Donor key,Food Ky, location key "+mdonorKey+" "+ mfoodKey +" " + mlocationKey);
                    locationHandler.addLocation(pickUpLocation, Constants.PICKUP_LOCATION, new DataStatus() {
                        @Override
                        public void dataCreated(String locationKey) {
                            super.dataCreated(locationKey);
                            mlocationKey = locationKey;
                            Log.d("Donation Controller 3","Donor key,Food Ky, location key "+mdonorKey+" "+ mfoodKey +" " + mlocationKey);
                            //Log.d("Donation Controller ","Donor key,Food Ky, location key "+mdonorKey+" "+ mfoodKey +" " + mlocationKey);
                            if(mdonorKey!=null&&mlocationKey!=null&&mfoodKey!=null){
                                DonationModel donationModel = new DonationModel(mdonorKey,mlocationKey,mfoodKey);
                                donationHandler.newDonation(donationModel,new DataStatus(){
                                    @Override
                                    public void dataCreated(Object  object) {
                                        Log.d("Donation Controller","donation created successfully!!");
                                        appDataStatus.dataCreated(object);
                                        super.dataCreated(object);
                                    }

                                    @Override
                                    public void errorOccured(String message) {
                                        Log.d("Donation Controller",message);
                                        appDataStatus.errorOccured(message);
                                    }
                                });
                            }
                            else
                            {
                                Log.d("Donation Controller","Some server error occured processing your request!");
                                appDataStatus.errorOccured("Some server error occured processing your request!");
                            }
                        }

                        @Override
                        public void errorOccured(String message) {
                            mlocationKey=null;
                            Log.d("Donation Controller",message);
                            appDataStatus.errorOccured("Donation Handler:"+message);
                        }
                    });
                }

                @Override
                public void errorOccured(String message) {
                    mfoodKey = null;
                    appDataStatus.errorOccured("Donation Handler: Some error occured while saving food details.Try again Later");
                }
            });


        }
        else{
            String errorMessage= "Please login to donate!!";
            Log.d("Donation Controller",errorMessage);
            appDataStatus.errorOccured(errorMessage);
        }
    }
}
