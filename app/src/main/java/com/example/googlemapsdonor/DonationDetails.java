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
import com.example.googlemapsdonor.firebasehandler.FBUserHandler;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationListModel;
import com.example.googlemapsdonor.models.DonationModel;
import com.example.googlemapsdonor.models.FoodModel;
import com.example.googlemapsdonor.models.UserModel;

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

    private static  final String CHANNEL_ID = "Donation accepted";
    private static  final String CHANNEL_NAME = "Donation Accepted";
    private static  final String CHANNEL_DESC= "Donation accepted notifocation";
    private NotificationManagerCompat notificationManager;

    private FBDonationHandler fbDonationHandler = new FBDonationHandler();
    private FBFoodHandler foodHandler = new FBFoodHandler();
    private FBUserHandler fbUserHandler = new FBUserHandler();

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
                                    foodItem = food.getFoodItem();
                                    shelfLife = food.getShelfLife();
                                    noOfPersons = food.getNoOfPersons();
                                    donorName = donor.getUserName();
                                    donorContact = donor.getMobileNo();
                                    donorCon.setText(donorContact);
                                    fooditem.setText(foodItem);

                                    shelf.setText(Integer.toString(shelfLife));

                                    persons.setText(Integer.toString(noOfPersons));

                                    donorname.setText(donorName);
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


        Log.d("fooditem",foodItem);

//        TextView donationTime = (TextView) findViewById(R.id.timeField);
//////        donationTime.setText(time);



        btnAccept = findViewById(R.id.acceptBtn);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(v);
                Intent intent = new Intent(getApplicationContext(),Ngo_Otp.class);
                startActivity(intent);

            }
        });

//        btnAccept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = "This is notification";
//                NotificationCompat.Builder builder =
//                        new NotificationCompat.Builder(DonationDetails.this,CHANNEL_ID)
//                                . setSmallIcon(R.drawable.ic_message_black)
//                        .setContentTitle("New Notification")
//                        .setContentText(message)
//                        .setAutoCancel(true);
//                Intent intent = new Intent(getApplicationContext(),NgoActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("message",message);
//                PendingIntent pendingIntent = PendingIntent.getActivity(DonationDetails.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(pendingIntent);
//
//                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(DonationDetails.this);
//              //  NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManagerCompat.notify(0,builder.build());
//            }
//        });
//
//        notificationManager = NotificationManagerCompat.from(this);


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
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
