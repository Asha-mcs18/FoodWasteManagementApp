package com.example.googlemapsdonor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.googlemapsdonor.controllers.DonationStatusController;
import com.example.googlemapsdonor.firebasehandler.FBUserAuthHandler;
import com.example.googlemapsdonor.firebasehandler.FBUserHandler;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.UserModel;
import com.example.googlemapsdonor.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //test
    private FBUserHandler fbUserHandler = new FBUserHandler();
    //test

    public void toDonorActivity(View view){
        Intent intent = new Intent(getApplicationContext(),DonorActivity.class);
     startActivity(intent);

    }
    public void toLoginActivity(View view){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);

    }
    public void toRegisterActivity(View view){
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }
    public void toNGOActivity(View view){
        Intent intent = new Intent(getApplicationContext(),NgoActivity.class);
        startActivity(intent);
    }

    public void toAboutActivity(View view){
        Intent intent = new Intent(getApplicationContext(),AboutActivity.class);
        startActivity(intent);
    }
    public void toHomeActivity(View view){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser!=null&&currentUser.getUid()!=null){
            Log.d("Donor role", "role is " +currentUser.getUid());
                fbUserHandler.readUserRoleByKey(currentUser.getUid(), new DataStatus() {
                    @Override
                    public void dataLoaded(String userRole) {
                        super.dataLoaded(userRole);
                        //UserModel userModel = ()userRole;
                        if(userRole!=null) {
                            Log.d("Donor role", "role is " + userRole);
                            if (userRole.equals(Constants.NGO)) {
                                Intent intent = new Intent(getApplicationContext(), NgoActivity.class);
                                startActivity(intent);
                            } else if (userRole.equals(Constants.DONOR)) {
                                Intent intent = new Intent(getApplicationContext(), DonorProfile.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void errorOccured(String message) {
                        Toast.makeText(MainActivity.this,"Somme Error",Toast.LENGTH_LONG).show();
                    }
                });

        }
    }
}
