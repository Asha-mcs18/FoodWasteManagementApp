package com.example.googlemapsdonor.firebasehandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationListModel;
import com.example.googlemapsdonor.models.DonationModel;
import com.example.googlemapsdonor.models.FoodModel;
import com.example.googlemapsdonor.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FBDonationHandler {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference donationRef;
    private List<DonationModel> donations;
    Calendar startDate = new GregorianCalendar();
    //Calendar endDate = new GregorianCalendar();
    int flag=0;
    private long i=0, size=0;

    //DonationModel donationModel;
    public FBDonationHandler(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        donationRef = firebaseDatabase.getReference(Constants.DONATION);
        donations  = new ArrayList<DonationModel>();
        //start date
        startDate.set(Calendar.HOUR_OF_DAY,0);
        startDate.set(Calendar.MINUTE,0);
        startDate.set(Calendar.SECOND,0);
        startDate.set(Calendar.MILLISECOND,0);
    }

    //done
    public void readDonations(final DataStatus dataStatus){
        donationRef.orderByChild("status").equalTo(Constants.NOT_ACCEPTED_YET).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i=0;
                size=0;
                donations.clear();
                Log.d("read donations", "donation key" +dataSnapshot.toString());
                List<String> keys = new ArrayList<>();
                size=dataSnapshot.getChildrenCount();
                Log.d("Donation Handler","size is "+size);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds!=null) {
                        String key = ds.getKey();
                        final DonationListModel donationModel = ds.getValue(DonationListModel.class);
                        keys.add(key);
                        //donations.add(donationModel);
                        String foodKey = donationModel.getFoodKey();
                        i++;
                        if (donationModel != null
                                && Long.parseLong(donationModel.getTimestampCreated().toString()) >= startDate.getTimeInMillis()) {
                            firebaseDatabase.getReference("Food").child(foodKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    FoodModel food = dataSnapshot.getValue(FoodModel.class);
                                    donationModel.setFoodItem(food.getFoodItem());
                                    donationModel.setNoOfPersons(food.getNoOfPersons());
                                    donations.add(donationModel);
                                    Log.d("Donation handler", "final donation  objet is " + donationModel.toString());
                                    if (i == size) {
                                        dataStatus.dataLoaded(donations);
                                        Log.d("read donations", "Outside donation handler for " + donations.size());
                                    }
                                    Log.d("read donations", "Value of i and size " + i + " " + size);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("read donations", "Some problem occured fetching list");
                dataStatus.errorOccured(databaseError.getMessage());
            }
        });
    }

    //check
    public void readDonationByKey(final String donationKey, final DataStatus dataStatus){
        donationRef.child(donationKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    DonationModel donation = dataSnapshot.getValue(DonationModel.class);
                    Log.d("Donation handler","inside donation"+donation.toString());
                    dataStatus.dataLoaded(donation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //done
    public void newDonation(final DonationModel donationModel, final DataStatus dataStatus){
        final String donorKey = donationModel.getDonorKey();
        String pickUpLocationKey = donationModel.getPickUpLocationKey();
        String foodKey= donationModel.getFoodKey();
        if(donorKey!=null&&pickUpLocationKey!=null&&foodKey!=null){
            //if this is the first donation made by donor
            Log.d("DBDonHand new","new  start time :"+startDate.getTimeInMillis());
            //Log.d("DBDonHand new","new end time :"+endDate.getTimeInMillis());
            donationRef.orderByChild(Constants.DONOR_KEY).equalTo(donorKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    flag=0;
                    Log.d("New Donation","new data snapshot is"+dataSnapshot.toString());
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        DonationModel dm= ds.getValue(DonationModel.class);
                        if(dm!=null
                                &&  Long.parseLong(dm.getTimestampCreated().toString())>=startDate.getTimeInMillis()){
                            Log.d("New Donation","new inside if condition hence donation already exist");
                            flag=1;
                        }
                    }
                    if(flag==0){
                        final String key = donationRef.push().getKey();
                        donationModel.setKey(key);
                        donationModel.setStatus(Constants.NOT_ACCEPTED_YET);
                        donationModel.setTimestampCreated(ServerValue.TIMESTAMP);
                        donationRef.child(key).setValue(donationModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("New Donation","New donation added at key"+donationModel.getKey());
                                        //donationRef.updateChildren()
                                        dataStatus.dataCreated(donationModel);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("New Donation","New donation Failure"+e.getMessage());
                                        dataStatus.errorOccured("New donation Failure"+e.getMessage());
                                    }
                                });
                    }
                    else{
                        Log.d("New Donation","new Cannot add more than one donation per day for key "+ donorKey);
                        dataStatus.errorOccured("new Cannot add more than one donation per day");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("New Donation","new error "+databaseError.getMessage());
                    dataStatus.errorOccured(databaseError.getMessage());
                }
            });
        }
    }

    //done
    public void addNgo(final String ngoKey, final String donationKey, final DataStatus dataStatus){
        donationRef.child(donationKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DonationModel donationModel = dataSnapshot.getValue(DonationModel.class);
                if(donationModel!=null&& donationModel.getStatus().equals(Constants.NOT_ACCEPTED_YET) ){
                    donationModel.setNgoKey(ngoKey);
                    donationModel.setStatus(Constants.ACCEPTED);
                    donationRef.child(donationKey).setValue(donationModel);
                    Log.d("get donation once","ngo key set is "+donationModel.getNgoKey());
                    dataStatus.dataUpdated("You have accepted the donation!!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //done
    public void changeStatusToComplete(final String donationKey, final DataStatus dataStatus ){
        donationRef.child(donationKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DonationModel donationModel = dataSnapshot.getValue(DonationModel.class);
                if(donationModel!=null&& donationModel.getStatus().equals(Constants.ACCEPTED) ){
                    //donationModel.setNgoKey(ngoKey);
                    donationModel.setStatus(Constants.SUCCESS);
                    donationRef.child(donationKey).setValue(donationModel);
                    Log.d("set complete ","donation done successfully!! "+donationModel.getKey());
                    dataStatus.dataUpdated("donation done successfully!! ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("set failed","error setting status"+donationKey);
                dataStatus.errorOccured(databaseError.getMessage());
            }
        });
    }

    //done
    public void changeStatusToFail(final String donationKey){
        donationRef.child(donationKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DonationModel donationModel = dataSnapshot.getValue(DonationModel.class);
                if(donationModel!=null&& donationModel.getStatus().equals(Constants.ACCEPTED) ){
                    //donationModel.setNgoKey(ngoKey);
                    donationModel.setStatus(Constants.FAILED);
                    donationRef.child(donationKey).setValue(donationModel);
                    Log.d("set failed ","donation failed!! "+donationModel.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("set failed","error setting status"+donationKey);
            }
        });
    }

    //done
    public void changeStatusToNA(final String donationKey){
        donationRef.child(donationKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DonationModel donationModel = dataSnapshot.getValue(DonationModel.class);
                if(donationModel!=null&& donationModel.getStatus().equals(Constants.NOT_ACCEPTED_YET)){
                    //donationModel.setNgoKey(ngoKey);
                    donationModel.setStatus(Constants.NOT_ACCEPTED_BY_ANYONE);
                    donationRef.child(donationKey).setValue(donationModel);
                    Log.d("set complete ","donation done successfully!! "+donationModel.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //done
    public void readDonationStatus(final String donorKey, final DataStatus dataStatus){
        Log.d("Donation Hleper", "start date is " + startDate.getTimeInMillis());
        //Log.d("Donation Hleper", "end date is " + endDate.getTimeInMillis());
        donationRef.orderByChild(Constants.DONOR_KEY).equalTo(donorKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DonationModel donationModel=null;
                Log.d("Donation Hleper","Data Snapshot is "+dataSnapshot.toString());
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    DonationModel dm= ds.getValue(DonationModel.class);
                    if(dm!=null
                            &&  Long.parseLong(dm.getTimestampCreated().toString())>=startDate.getTimeInMillis()){
                        donationModel = ds.getValue(DonationModel.class);
                    }
                }
                if(donationModel!=null){
                    Log.d("Donation Hleper", "Data Snapshot is " + donationModel.toString());
                    dataStatus.dataLoaded(donationModel.getStatus(),donationModel.getOtp());
                }
                else{
                    Log.d("Donation Hleper", "Data Snapshot is " + donationModel);
                    dataStatus.dataLoaded("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Donation Hleper","Data Snapshot is "+databaseError.getMessage());
                dataStatus.errorOccured(databaseError.getMessage());
            }
        });
    }
}
