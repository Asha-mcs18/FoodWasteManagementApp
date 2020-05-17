package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googlemapsdonor.firebasehandler.FBDonationHandler;
import com.example.googlemapsdonor.firebasehandler.FBFoodHandler;
import com.example.googlemapsdonor.firebasehandler.FBLocationHandler;
import com.example.googlemapsdonor.firebasehandler.FBUserHandler;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationListModel;
import com.example.googlemapsdonor.models.DonationModel;
import com.example.googlemapsdonor.models.FoodModel;
import com.example.googlemapsdonor.models.LocationModel;
import com.example.googlemapsdonor.models.UserModel;
import com.example.googlemapsdonor.utils.Constants;

public class Ngo_Otp extends AppCompatActivity {
    EditText otp ;
    int mOTP;
    int donorOTP;
    String donationKey = "";
    String foodItem = "";
    int shelfLife ;
    int noOfPersons ;
    String time = "12:04";
    String donorName = " " ;
    String donorContact =" " ;
    Button btnAccept ;
    private FBDonationHandler fbDonationHandler = new FBDonationHandler();
    private FBFoodHandler foodHandler = new FBFoodHandler();
    private FBUserHandler fbUserHandler = new FBUserHandler();
    private FBLocationHandler locationHandler = new FBLocationHandler();

    private FoodModel food;
    private UserModel donor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo__otp);
        otp = (EditText) findViewById(R.id.otpField);
        donationKey = getIntent().getStringExtra("donationKey");

        final TextView fooditem = (TextView) findViewById(R.id.foodItemField);
        final TextView shelf = (TextView) findViewById(R.id.shelfLifeField);
        final TextView persons = (TextView) findViewById(R.id.noOfPersonsField);
        final TextView donorname = (TextView) findViewById(R.id.donorNameField);
        final TextView donorCon = (TextView) findViewById(R.id.donorContactField);
        final TextView pickupTime = (TextView)findViewById(R.id.timeField);
        final DonationListModel donation = (DonationListModel) getIntent().getSerializableExtra("DonationModel");


        Log.i("DONATION KEY","DONATION KEY"+donationKey);
        if(donation!=null) {
            donorOTP = donation.getOtp();
        }
        if(donation!=null&& donation.getFoodKey()!=null&&donation.getDonorKey()!=null){
            foodHandler.getFoodItem(donation.getFoodKey(), new DataStatus() {
                @Override
                public void dataLoaded(Object fObject) {
                    super.dataLoaded(fObject);
                    food = (FoodModel) fObject;
                    Log.d("Donation Details","inside food"+food.toString());
                    fbUserHandler.readDonorByKey(donation.getDonorKey(), new DataStatus() {
                        @Override
                        public void dataLoaded(Object donorObject) {
                            super.dataLoaded(donorObject);
                            donor = (UserModel) donorObject;
                            Log.d("Donation Details","inside donor"+food.getFoodItem()+"   "+donor.toString());
                            locationHandler.readLocationForKey(donation.getPickUpLocationKey(), Constants.PICKUP_LOCATION, new DataStatus() {
                                @Override
                                public void dataLoaded(Object object) {
                                    super.dataLoaded(object);
                                    LocationModel pickUpLocation = (LocationModel) object;
                                    Log.d("Donation Details","Location is "+pickUpLocation.toString());
                                    foodItem = food.getFoodItem();
                                    shelfLife = food.getShelfLife();
                                    noOfPersons = food.getNoOfPersons();
                                    donorName = donor.getUserName();
                                    donorContact = donor.getMobileNo();
                                    donorCon.setText(donorContact);
                                    fooditem.setText(foodItem);
                                    donationKey=donation.getKey();
                                    shelf.setText(Integer.toString(shelfLife));
                                    persons.setText(Integer.toString(noOfPersons));
                                    donorname.setText(donorName);
                                    pickupTime.setText(pickUpLocation.getTime());
                                    Log.d("fooditem",foodItem);
                                }

                                @Override
                                public void errorOccured(String message) {

                                }
                            });
                        }

                        @Override
                        public void errorOccured(String message) {

                        }
                    });
                }

                @Override
                public void errorOccured(String message) {

                }
            });
        }
    }

    public void checkOtp(View v)
    {
        Log.d("otp",""+otp.getText().toString());
        Log.d("otp","donation key "+donationKey);
        Log.d("donor otp","donor otp"+donorOTP);
        mOTP = Integer.parseInt(otp.getText().toString());
        if(mOTP!=0&&donorOTP!=0&&mOTP==donorOTP){
            fbDonationHandler.changeStatusToComplete(donationKey, new DataStatus() {
                @Override
                public void dataUpdated(String message) {
                    super.dataUpdated(message);
                    Toast.makeText(Ngo_Otp.this,message,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void errorOccured(String message) {
                    Toast.makeText(Ngo_Otp.this,message,Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(Ngo_Otp.this,"Invalid OTP!",Toast.LENGTH_LONG).show();
        }
    }

    public void initialize(){

    }
}
