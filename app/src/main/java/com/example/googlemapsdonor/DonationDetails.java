package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.googlemapsdonor.utils.Notifications.CHANNEL_1_ID;
import static com.example.googlemapsdonor.utils.Notifications.CHANNEL_2_ID;

public class DonationDetails extends AppCompatActivity {
    double latitude = 28.649931099999996;
    double longitude = 77.2684403;
    String foodItem = "";
    int shelfLife ;
    int noOfPersons ;
    String time = "12:04";
    String donorName = " " ;
    String donorContact =" " ;
    Button btnAccept ;
    String donationKey="";
    private static  final String CHANNEL_ID = "Donation accepted";
    private static  final String CHANNEL_NAME = "Donation Accepted";
    private static  final String CHANNEL_DESC= "Donation accepted notifocation";
    private NotificationManagerCompat notificationManager;

    private FBDonationHandler fbDonationHandler = new FBDonationHandler();
    private FBFoodHandler foodHandler = new FBFoodHandler();
    private FBUserHandler fbUserHandler = new FBUserHandler();
    private FBLocationHandler locationHandler = new FBLocationHandler();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private FoodModel food;
    private UserModel donor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);
        notificationManager = NotificationManagerCompat.from(this);
        final TextView fooditem = (TextView) findViewById(R.id.foodItemField);
        final TextView shelf = (TextView) findViewById(R.id.shelfLifeField);
        final TextView persons = (TextView) findViewById(R.id.noOfPersonsField);
        final TextView donorname = (TextView) findViewById(R.id.donorNameField);
        final TextView donorCon = (TextView) findViewById(R.id.donorContactField);
        final TextView pickupTime = (TextView)findViewById(R.id.timeField);
        final  DonationModel donation = (DonationListModel) getIntent().getSerializableExtra("DonationModel");
        Log.i("DONATION KEY","DONATION KEY"+donation.getKey());
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
                                    locationHandler.readLocationForKey(donation.getPickUpLocationKey(),Constants.PICKUP_LOCATION, new DataStatus() {
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
                                            longitude = Double.parseDouble(pickUpLocation.getLatitude().toString());
                                            latitude = Double.parseDouble(pickUpLocation.getLongitute().toString());
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


        Log.d("fooditem",foodItem);
        btnAccept = findViewById(R.id.acceptBtn);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("get donation once","current user is "+Constants.currentUser);


                if(currentUser.getUid()!= null) {
                    fbDonationHandler.addNgo(currentUser.getUid(),donationKey, new DataStatus() {
                        @Override
                        public void dataUpdated(String message) {
                            super.dataUpdated(message);
                            Log.d("Donation details","status"+message);
                            sendNotification(v);
                            Intent intent = new Intent(getApplicationContext(), Ngo_Otp.class);
                            Log.d("DonationDetails","Donor otp"+donation.getOtp());
                            intent.putExtra("otp",donation.getOtp());
                            intent.putExtra("donationKey",donation.getKey());
                            intent.putExtra("DonationModel",donation);
                            startActivity(intent);
                        }

                        @Override
                        public void errorOccured(String message) {
                            Toast.makeText(DonationDetails.this,message,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(DonationDetails.this,"Please Login First!",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void sendNotification(View v) {
        String title = "You have accepted Donation";
        String message = "Donation accepted successfully";
        Log.i("onsend","onsend");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_message_black)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);

    }
    public void onGoToMaps(View view){
        Intent intent = new Intent(getApplicationContext(),PickupLocationActivity.class);
        Log.i("lat",""+latitude+ longitude);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
