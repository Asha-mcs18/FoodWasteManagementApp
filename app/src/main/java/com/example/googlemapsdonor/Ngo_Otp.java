package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googlemapsdonor.firebasehandler.FBDonationHandler;
import com.example.googlemapsdonor.models.DataStatus;

public class Ngo_Otp extends AppCompatActivity {
  EditText otp ;
    int mOTP;
    int donorOTP;
    String donationKey = "";

    private FBDonationHandler fbDonationHandler = new FBDonationHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo__otp);
        otp = (EditText) findViewById(R.id.otp_Field);
        donorOTP =  getIntent().getIntExtra("otp",0);
        donationKey = getIntent().getStringExtra("donationKey");
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
    }
}
