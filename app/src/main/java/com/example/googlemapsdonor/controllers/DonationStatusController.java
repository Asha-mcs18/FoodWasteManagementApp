package com.example.googlemapsdonor.controllers;

import android.util.Log;

import com.example.googlemapsdonor.firebasehandler.FBDonationHandler;
import com.example.googlemapsdonor.firebasehandler.FBRoleHandler;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

public class DonationStatusController {
    private String currentDonor;
    private FBRoleHandler fbRoleHandler;
    private FBDonationHandler fbDonationHandler;


    public DonationStatusController(){
        fbRoleHandler = new FBRoleHandler();
        fbDonationHandler = new FBDonationHandler();
    }

    public void setCurrentDonor(){
        this.currentDonor = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    public void donorStatus(final DataStatus appDataStatus) {
        setCurrentDonor();
        if(currentDonor!=null){
            Log.d("DonationStatusControler"," current userid is "+currentDonor);
            fbRoleHandler.getRole(currentDonor, new DataStatus() {
                @Override
                public void dataLoaded(String role) {
                    super.dataLoaded(role);
                    Log.d("DonationStatusControler","donations status is "+role);
                    if(role.equals(Constants.DONOR)){
                        fbDonationHandler.readDonationStatus(currentDonor, new DataStatus() {
                            @Override
                            public void dataLoaded(String status) {
                                super.dataLoaded(status);
                                Log.d("DonationStatusControler","donations status is "+status);
                                appDataStatus.dataLoaded(status);
                            }

                            @Override
                            public void errorOccured(String message) {
                                Log.d("DonationStatusControler","donations status is "+message);
                                appDataStatus.dataLoaded(message);
                            }
                        });
                    }
                }

                @Override
                public void errorOccured(String message) {
                    Log.d("DonationStatusControler","donations status is "+message);
                    appDataStatus.dataLoaded(message);
                }
            });
        }
    }
}
