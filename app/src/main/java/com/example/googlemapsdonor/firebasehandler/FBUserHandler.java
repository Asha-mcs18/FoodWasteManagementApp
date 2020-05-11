package com.example.googlemapsdonor.firebasehandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.UserModel;
import com.example.googlemapsdonor.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FBUserHandler {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;
    private DatabaseReference fullPath;
    private FBUserAuthHandler userAuthHandler;
    private DatabaseReference roleRef;
    private DatabaseReference dbRef;

    public FBUserHandler(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef  = firebaseDatabase.getReference(Constants.USER);
        roleRef=  firebaseDatabase.getReference(Constants.ROLE);
        userAuthHandler = new FBUserAuthHandler();
        dbRef = firebaseDatabase.getReference();
    }

    //user will call this and it will direct for auth
    public void addUser(final UserModel user,final String password, final DataStatus dataStatus ){
        fullPath = userRef.child(user.getRole());
        if(user!=null&&password!=null) {
            //check if user with same username already exist
            Log.d("User ADD"," User added method called from register actvity: "
                    + fullPath.orderByChild(Constants.USER_NAME).equalTo(user.getUserName()));
            fullPath.orderByChild(Constants.USER_NAME).equalTo(user.getUserName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //user with same username already exist hence error message
                            if (dataSnapshot.getValue() != null) {
                                dataStatus.errorOccured("User with this email address already exist");
                                return;
                            }
                            else {
                                userAuthHandler.createNewUser(user.getUserName(), password, new DataStatus() {
                                    @Override
                                    public void dataCreated(String userId) {
                                        super.dataCreated(userId);
                                        Map<String, Object> updates = new HashMap<String, Object>();
                                        user.setUserKey(userId);
                                        String userPathKey = "/Users/"+user.getRole()+"/"+user.getUserKey();
                                        String rolePathKey = "/Role/"+user.getUserKey();
                                        Log.d("add User "," user path "+userPathKey);
                                        Log.d("add User "," role path "+rolePathKey);
                                        updates.put(userPathKey,user);
                                        updates.put(rolePathKey,user.getRole());
                                        String successMessage = "User has been successfully registered. Login to continue!";
                                        dbRef.updateChildren(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("add user","user key is "+user.getUserKey());
                                                        dataStatus.dataCreated("User registered successfully!!!");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    String errorMessage = "ERROR OCCURED WHILE CREATING YOUR ACCOUNT. PLEASE TRY AGAIN LATER";
                                                    Log.d("add User ",e.getMessage());
                                                    dataStatus.errorOccured(e.getMessage());
                                                }
                                                });
                                        }
                                    @Override
                                    public void errorOccured(String message) {
                                        Log.d("ADD USER",message);
                                        dataStatus.errorOccured(message);
                                    }
                                });
                            }
                        }
/*
   //FirebaseUser currentUser = (FirebaseUser) object;
                                //UserModel userModel = new UserModel(user.getUserKey(),user.getUserName(), user.getRole(),user.getMobileNo());

                        //String userKey = userRef.push().getKey();
                        //fullPath.child(userKey).setValue(user);
                        //String roleKey =  roleRef.push().getKey();
                        //Map<String,String> userRole = new HashMap<String, String>();
                        //userRole.put(user.getUserName(),user.getRole());
                        //roleRef.child(user.getUserName()).setValue(user.getRole());

                }*/
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    String message = "Some Error occurred creating account! Please try again later";
                    Log.d("ADD USER",message);
                    dataStatus.errorOccured(message);
                    return;
                }
            });

        }
    }

    public void readUser(final String userName, final String password, final DataStatus activityDataStatus){
        userAuthHandler.signInUser(userName, password,new DataStatus(){
            @Override
            public void dataLoaded(final String currentUserId) {
                super.dataLoaded(currentUserId);
                roleRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String role = dataSnapshot.getValue(String.class);
                        Log.d("User Handler","Values inside datasnapshot is "+dataSnapshot.toString());
                        Log.d("User Handler","Inside read user role is "+role);
                        if(role!=null){
                            //read details of user
                            userRef.child(role).child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserModel userLoggedIn = dataSnapshot.getValue(UserModel.class);
                                    Log.d("AUTH HANDLER",userLoggedIn.getUserName());
                                    activityDataStatus.dataLoaded(userLoggedIn);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d("USER AUTH HANDLER",databaseError.getMessage());
                                    activityDataStatus.errorOccured(databaseError.getMessage());
                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("USER AUTH HANDLER",databaseError.getMessage());
                        activityDataStatus.errorOccured(databaseError.getMessage());
                    }
                });
            }

            @Override
            public void errorOccured(String message) {
                Log.d("USER AUTH HANDLER",message);
                activityDataStatus.errorOccured(message);
            }
        });
        /*fullPath = userRef.child(role);
        fullPath.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("read user","key and role is "+dataSnapshot.exists()+"  "+dataSnapshot.toString());
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if(user!=null) {
                    Log.d("read user", "user key read for role" + user.getRole());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("read user","error reading user");
            }
        });*/
    }

    public void readUserRoleByKey(final String key, final DataStatus dataStatus){
        roleRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user =  dataSnapshot.getValue(String.class);
                dataStatus.dataLoaded(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateUser(final UserModel updatedUser){
        fullPath =userRef.child(updatedUser.getRole());
        fullPath.orderByKey().equalTo(updatedUser.getUserKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                fullPath.child(updatedUser.getUserKey()).setValue(updatedUser);
                Log.d("updated user","user updated role is "+updatedUser.getRole());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("updated user","error performing update operation");
            }
        });
    }

//    public void isRoleMatching(String key,String role){
//        DatabaseReference fullPath = userRef.child(role);
//        fullPath.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot!=null){
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
