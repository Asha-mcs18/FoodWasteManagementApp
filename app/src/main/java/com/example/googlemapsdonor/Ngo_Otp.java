package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Ngo_Otp extends AppCompatActivity {
  EditText otp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo__otp);
         otp = (EditText) findViewById(R.id.otp_Field);

    }
    public void checkOtp(View v)
    {
        Log.d("otp",""+otp.getText().toString());
    }
}
