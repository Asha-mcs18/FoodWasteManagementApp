package com.example.googlemapsdonor.firebasehandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationModel;
import com.example.googlemapsdonor.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FBRoleHandler {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference roleRef;
    //private List<DonationModel> donations;

    //DonationModel donationModel;

    public FBRoleHandler(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        roleRef = firebaseDatabase.getReference(Constants.ROLE);
        //donations  = new ArrayList<DonationModel>();
    }

    public void getRole(String userKey, final DataStatus dataStatus){
        roleRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String role  = dataSnapshot.getValue(String.class);
                Log.d("FB Role Handler",role);
                dataStatus.dataLoaded(role);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FB Role Handler",databaseError.getMessage());
                dataStatus.errorOccured(databaseError.getMessage());
            }
        });
    }



    //for loop in ngo activity calling food item for every food key and redering it on the screen

}
