package com.example.googlemapsdonor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.googlemapsdonor.controllers.DonationController;
import com.example.googlemapsdonor.firebasehandler.FBDonationHandler;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationModel;
import com.example.googlemapsdonor.models.FoodModel;
import com.example.googlemapsdonor.models.LocationModel;

public class DonorActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GETMESSAGE = 1014;
    String latitude;
    String longitude;
    TimePicker picker;

    int minutes=0;
    int hours=0;
    private DonationController donationController = new DonationController();

    public void toMapActivity(View view){
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//     startActivity(intent);
        startActivityForResult(intent , REQUEST_CODE_GETMESSAGE);
    }
    public void toSearchAddress(View view){
        Intent intent = new Intent(getApplicationContext(),SearchAddressMap.class);
//     startActivity(intent);
        startActivityForResult(intent , REQUEST_CODE_GETMESSAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_GETMESSAGE:
                if (resultCode == Activity.RESULT_OK) {
                    latitude = data.getStringExtra("latitude");
                    longitude = data.getStringExtra("longitude");

                } else {
                    Log.i("Activity cancelled", "cancelled");
                }
        }

    }
    public void onSubmit(View view){
        final EditText foodItem = (EditText) findViewById(R.id.FoodItemField);
        final EditText shelfLife =  (EditText) findViewById(R.id.shelfLifeField);
        final EditText noOfPersons = (EditText) findViewById(R.id.NoOfPersonsField);
        picker=(TimePicker)findViewById(R.id.timePicker1);
        hours = picker.getHour();
        minutes = picker.getMinute();
        //final String hours = Integer.toString(picker.getHour());
        //final String minutes = Integer.toString(picker.getMinute());
        Log.i("ALL FOOD DETAILS","ENTERED BY DONOR");
        Log.i("FOOD ITEM", foodItem.getText().toString());
        Log.i("SHELF LIFE", shelfLife.getText().toString());
        Log.i("NO OF PERSONS",noOfPersons.getText().toString());
        Log.i("LATITUDE ", latitude);
        Log.i("LONGITUDE ", longitude);

//     startActivityForResult(intent , REQUEST_CODE_GETMESSAGE);

        Button btn =(Button) findViewById(R.id.submitRequest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(),DonationsStatus.class);
                //firebase connection code
                String mfoodItem = foodItem.getText().toString();
                int mshelfLife = Integer.parseInt(shelfLife.getText().toString());
                int mNoOfPersons = Integer.parseInt(noOfPersons.getText().toString());
                Double mlatitude = Double.parseDouble(latitude);
                Double mlongitude = Double.parseDouble(longitude);
                String time = hours +":"+minutes;
                boolean isValid = validateInput(mfoodItem,mshelfLife,mNoOfPersons,mlatitude,mlongitude,hours,minutes);
                if(isValid){
                    FoodModel food = new FoodModel(mfoodItem,mshelfLife,mNoOfPersons);
                    Log.d("Donor Activity","Food object created ");
                    LocationModel pickUpLocation = new LocationModel(mlatitude,mlongitude,time);
                    Log.d("Donor Activity","Location object created "+time);
                    donationController.createNewDonation(food, pickUpLocation, new DataStatus() {
                        @Override
                        public void dataCreated(Object object) {
                            super.dataCreated(object);
                            DonationModel donation = (DonationModel) object;
                            Toast.makeText(DonorActivity.this,"Donation created successfully!",Toast.LENGTH_LONG).show();
                            //FoodModel food = new FoodModel(foodItem.getText().toString(),shelfLife.
                            intent.putExtra("DonationModel",donation);
                            startActivity(intent);
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }
                        @Override
                        public void errorOccured(String message) {
                            Log.d("Donor Activity",message);
                            Toast.makeText(DonorActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validateInput(String mfoodItem, int mshelfLife, int mNoOfPersons, Double mlatitude, Double mlongitude, int hours, int minutes) {
        if(mfoodItem!="" && mshelfLife>3&&mNoOfPersons>10&&mlatitude!=0&&mlongitude!=0&&hours>0&&hours<23&&minutes>0){
            return true;
        }
        return false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
    }
}

//
//                intent.putExtra("foodItem", foodItem.getText().toString());
//                        intent.putExtra("shelfLife", shelfLife.getText().toString());
//                        intent.putExtra("noOfPersons",noOfPersons.getText().toString());
//                        intent.putExtra("latitude",latitude);
//                        intent.putExtra("longitude",longitude);
//                        intent.putExtra("hours",hours);
////                        intent.putExtra("minutes",minutes);