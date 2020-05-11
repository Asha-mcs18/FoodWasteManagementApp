package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    String aboutText;
    TextView aboutDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutText = "The importance of food is Obvious and essential. Food is the third most important thing for living beings. Food affect our body and mental and social health because each food or liquid contain particular nutrition which is very important for our physical and mental development. On the other hand, it is estimated that nearly one third of the food produced in the world for human consumption every year gets lost or wasted. Such food might be waste for someone but it can be the most delicious meal to someone else.";

        aboutDetails = findViewById(R.id.aboutInfo);
        aboutDetails.setText(aboutText);
    }
}
