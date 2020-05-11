package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.googlemapsdonor.firebasehandler.FBUserHandler;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.UserModel;

public class RegisterActivity extends AppCompatActivity {
    private FBUserHandler fbUserHandler = new FBUserHandler();
    String userRole;
    public void onRegister(View view){
        final EditText email = (EditText) findViewById(R.id.emailField);
        final EditText password =  (EditText) findViewById(R.id.password_field);
        final EditText contact =  (EditText) findViewById(R.id.phoneField);

        Log.i("USER ROLE", userRole);
        Log.i("Email",email.getText().toString());
        Log.i("Password",password.getText().toString());
        Log.i("Contact num", contact.getText().toString());

        //new user creation
        UserModel newUser = new UserModel();
        //newUser.setPassword(password.getText().toString());
        newUser.setRole(userRole);
        newUser.setUserName(email.getText().toString());
        newUser.setMobileNo(contact.getText().toString());
        if(newUser.isValid()) {
            Log.d("USER REGISTRATION","User details are valid!");
            fbUserHandler.addUser(newUser,password.getText().toString(),new DataStatus() {
                @Override
                public void dataCreated(String message) {
                    super.dataCreated(message);
                    Log.d("USER REGISTRATION",message);
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void errorOccured(String message) {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        else{
            errorMessage("Please provide details correctly!!");
        }
    }

    public void errorMessage(String errorMessage){
        Log.d("USER REGISTRATION",errorMessage);
        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner spin = (Spinner) findViewById(R.id.spin);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userRole = parent.getSelectedItem().toString();
                Log.i("user role",parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }
}
