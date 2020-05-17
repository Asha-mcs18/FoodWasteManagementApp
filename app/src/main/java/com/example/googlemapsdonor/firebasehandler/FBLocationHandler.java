package com.example.googlemapsdonor.firebasehandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.LocationModel;
import com.example.googlemapsdonor.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FBLocationHandler {

    private FirebaseDatabase databaseRef = FirebaseDatabase.getInstance();
    DatabaseReference locRef  = databaseRef.getReference(Constants.LOCATION);
    DatabaseReference fullPathRef;
    private LocationModel location;

    public void getLocation(String typeOfLocation){
        Log.d("Path","Location path is"+ locRef);
        //fullPathRef= locRef.child(typeOfLocation).child(key);
        Log.d("Path","NGO Location path is"+ fullPathRef);
        /*fullPathRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LocationModel locationModel = dataSnapshot.getValue(LocationModel.class);
                Log.d("location get","latitue and longitude are"+locationModel.getLatitute()+" "+locationModel.getLongitute());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        DatabaseReference ngoRef = locRef.child(Constants.NGO);
        ngoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String key = ds.getKey();
                    LocationModel ngoLoc = ds.getValue(LocationModel.class);
                    Log.d("Key","ngo key is "+key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //done
    public void readLocationForKey(String key, String typeOfLocation, final DataStatus dataStatus){
        fullPathRef =null;
        fullPathRef= locRef.child(typeOfLocation).child(key);
        //Log.d("read Location","Location read for key"+key);
        fullPathRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                LocationModel loc = dataSnapshot.getValue(LocationModel.class);
                Log.d("Location for a type","location key is "+ key);
                dataStatus.dataLoaded(loc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //done
    public void addLocation(final LocationModel locationModel , String typeOfLocation, final DataStatus dataStatus){
        fullPathRef =null;
        fullPathRef = locRef.child(typeOfLocation);
        String key= fullPathRef.push().getKey();
        locationModel.setLocationKey(key);
        if(typeOfLocation.equals(Constants.PICKUP_LOCATION)){
            locationModel.setStatus(true);
        }
        fullPathRef.child(key).setValue(locationModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Location added","Location added at key"+locationModel.getLocationKey());
                            dataStatus.dataCreated(locationModel.getLocationKey());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Location added","Location failure"+e.getMessage());
                            dataStatus.errorOccured(e.getMessage());
                        }
                    });
    }

    //check
    public void updateLocation(LocationModel updatedLocation,String typeOfLocation){
        fullPathRef =null;
        fullPathRef = locRef.child(typeOfLocation);
        Log.d("udpate loc","location update for key"+fullPathRef);
        if(updatedLocation!=null) {
            location = getLocForKey(updatedLocation.getLocationKey(),typeOfLocation);
            if(location!=null){
                fullPathRef.setValue(updatedLocation);
                Log.d("updte ","Loc updated successfully!!");
            }
        }
    }

    //check
    public LocationModel getLocForKey(String key , String typeOfLocation){
        fullPathRef =null;
        fullPathRef = locRef.child(typeOfLocation).child(key);
        //Log.d("get loc","location get for key"+fullPathRef);
        fullPathRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                location = null;
                location = dataSnapshot.getValue(LocationModel.class);
                if(location!=null) {
                    Log.d("get loc for key", "location get for latitude" + location.getLocationKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(location!=null) {
            Log.d("get loc method", "returned location is " + location.getLocationKey());
        }
        return location;
    }

    /*public void testConn(){
        dbRef.child("first Message").setValue("my new message");
        Log.d("firbase","Location handler testing"+dbRef);
    }*/

}

/*
Testing for location
FBLocationHandler fbLocationHandler = new FBLocationHandler();
     //fbLocationHandler.getLocation( Constants.NGO);
     //fbLocationHandler.readLocationForKey("-M58zJqLyKMTWu5qRUEa",Constants.NGO);
     //LocationModel locationModel = new LocationModel(123.123,2123.123,"12:12:23");
     //fbLocationHandler.addLocation(locationModel,Constants.NGO);

     LocationModel updte =  fbLocationHandler.getLocForKey("-M58zJqLyKMTWu5qRUEa",Constants.NGO);
     if(updte!=null) {
         Log.d("Main Activity","message for get loc is "+updte.getLocationKey());
     }
* */