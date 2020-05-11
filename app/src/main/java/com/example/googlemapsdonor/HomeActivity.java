package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    String homeText;
    TextView homeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeText = "As a donor:  If a user wants to donate the food, he/she needs to register in our app through email id and provide some personal like name of the donor, contact no. After that he/she needs to provide the address from to pick the food, quantity of the food. \n" +
                "As NGO:  If NGO want to associate with our app, it also need to get register in our app. ";

        homeDetails = findViewById(R.id.hometInfo);
        homeDetails.setText(homeText);
    }

}
