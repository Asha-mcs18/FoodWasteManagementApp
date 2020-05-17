package com.example.googlemapsdonor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import static com.example.googlemapsdonor.utils.Notifications.CHANNEL_1_ID;

import android.widget.TextView;

import com.example.googlemapsdonor.controllers.DonationStatusController;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationModel;
import com.example.googlemapsdonor.utils.Constants;

public class DonationsStatus extends AppCompatActivity {

    public static final int REQUEST_CODE_GETMESSAGE = 1014;
    private NotificationManagerCompat notificationManager;
    private TextView mstatus;
    private String donationStatus= "";


    @Override
    protected void onStart() {
        super.onStart();
        mstatus = findViewById(R.id.mstatus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_status);
//        sendNotification();
        Log.i("Donation Status","status");
        notificationManager = NotificationManagerCompat.from(this);
        DonationStatusController statusController = new DonationStatusController();
        //
        if(donationStatus==null||donationStatus.equals("")){
            mstatus= findViewById(R.id.mstatus);
            mstatus.setText("Please make a donation first");
        }
        statusController.donorStatus(new DataStatus() {
            @Override
            public void dataLoaded(String status,final int otp) {
                super.dataLoaded(status);
                donationStatus=status;
                if(donationStatus==null||donationStatus.equals("")){
                   mstatus.setText("Please make a donation first");
                }
                mstatus.setText(donationStatus);
                Log.d("DonationStatusCOntrole", "Data Snapshot is " + status);
                Log.d("DonationStatusCOntrole", "Data Snapshot is " + otp);
                if(donationStatus!=null&&donationStatus.equals(Constants.ACCEPTED)){
                    sendNotification(otp);
                }
            }

            @Override
            public void errorOccured(String message) {

            }
        });
    }

    public void sendNotification(int otp) {
        String title = "Give this OTP to NGO";
        String message = "Your OTP is "+ otp;
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
}
