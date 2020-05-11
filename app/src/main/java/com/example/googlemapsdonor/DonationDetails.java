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

import static com.example.googlemapsdonor.utils.Notifications.CHANNEL_1_ID;

public class DonationDetails extends AppCompatActivity {
    double latitude = 28.649931099999996;
    double longitude = 77.2684403;
    String foodItem = "Burgers";
    int shelfLife = 3;
    int noOfPersons = 5;
    String time = "12:04";
    String donorName = "Burger King";
    String donorContact = "1234567896";
    Button btnAccept ;

    private static  final String CHANNEL_ID = "Donation accepted";
    private static  final String CHANNEL_NAME = "Donation Accepted";
    private static  final String CHANNEL_DESC= "Donation accepted notifocation";
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);
        String donationKey =getIntent().getStringExtra("donationKey");
        Log.i("DONATION KEY",donationKey);
        TextView food = (TextView) findViewById(R.id.foodItemField);
        food.setText(foodItem);
        TextView shelf = (TextView) findViewById(R.id.shelfLifeField);
        shelf.setText(Integer.toString(shelfLife));
        TextView persons = (TextView) findViewById(R.id.noOfPersonsField);
        persons.setText(Integer.toString(noOfPersons));
        TextView donor = (TextView) findViewById(R.id.donorNameField);
        donor.setText(donorName);
        TextView donationTime = (TextView) findViewById(R.id.timeField);
        donationTime.setText(time);
        TextView donorCon = (TextView) findViewById(R.id.donorContactField);
        donorCon.setText(donorContact);

        btnAccept = findViewById(R.id.acceptBtn);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "This is notification";
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(DonationDetails.this,CHANNEL_ID)
                                . setSmallIcon(R.drawable.ic_message_black)
                        .setContentTitle("New Notification")
                        .setContentText(message)
                        .setAutoCancel(true);
                Intent intent = new Intent(getApplicationContext(),NgoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message",message);
                PendingIntent pendingIntent = PendingIntent.getActivity(DonationDetails.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(DonationDetails.this);
              //  NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManagerCompat.notify(0,builder.build());
            }
        });

        notificationManager = NotificationManagerCompat.from(this);


    }
    public void sendNotification(View v) {
        String title = "Donation Accepted";
        String message = "You accepted the donation";
        Log.i("onsend","onsend");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_message_black)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
        Intent intent = new Intent(getApplicationContext(),NgoActivity.class);
        startActivity(intent);
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
