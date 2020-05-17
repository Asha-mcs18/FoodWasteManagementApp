package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Ngo_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_profile);
    }
    public void onViewAllDonations(View v){
        Intent intent = new Intent(getApplicationContext(), NgoActivity.class);
        startActivity(intent);

    }
    public void myDonations(View v){
        Intent intent = new Intent(getApplicationContext(), MyDonations.class);
        startActivity(intent);

    }
}
