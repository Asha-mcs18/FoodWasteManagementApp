package com.example.googlemapsdonor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.googlemapsdonor.controllers.DonationListController;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationListModel;
import com.example.googlemapsdonor.models.DonationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NgoActivity<fragment1> extends AppCompatActivity {
    ArrayList <String> foodItemList = new ArrayList<>() ;
    HashMap<Integer ,String> donations=new HashMap<Integer,String>();
    private List<DonationListModel> mdonationList =null;
    private DonationListController donationListController = new DonationListController();

    @Override
    protected void onStart() {
        super.onStart();
        donationListController.getDonationList(new DataStatus() {
            @Override
            public void dataLoaded(List<?> donations) {
                super.dataLoaded(donations);
                Log.d("NGO ACTIVITY", "ngo activity Donation List Item after food added");
                List<DonationListModel> donationList = (List<DonationListModel>)(List<?>) donations;
                mdonationList = donationList;
                for(DonationListModel d: donationList){
                    Log.d("NGO ACTIVITY", "added to list" +d.getFoodItem());
                }
                addItemstoList();
            }

            @Override
            public void dataLoaded(String object) {
                super.dataLoaded(object);

                Log.d("NGO ACTIVITY"," Ngo actitivity"+object);
            }

            @Override
            public void errorOccured(String message) {
                Log.d("Ngo Activity","Donation List Loaded falied"+message);
                Toast.makeText(NgoActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }
    public void addItemstoList(){
        donations.clear();
        foodItemList.clear();
        int k = 0;
        for(DonationListModel d: mdonationList){
            Log.d("NGO ACTIVITY", "Food details added to list" +d.getFoodItem()+d.getNoOfPersons());
            String value = d.getFoodKey();
            String foodDetails =  "Food Item:  " +d.getFoodItem() + "    Persons: "+ d.getNoOfPersons();
            donations.put(k,value);
            foodItemList.add(foodDetails);
            k++;
        }

        final ListView donationList = (ListView) findViewById(R.id.donationsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,foodItemList);
        donationList.setAdapter(adapter);
        donationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DonationDetails.class);
                String donationKey = donations.get(position);
                intent.putExtra("DonationModel",mdonationList.get(position));
                startActivity(intent);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);

        Log.d("NGO ACTIVITY", "Donation List Items are : ");
        if(mdonationList!=null){
            for (DonationListModel donation:mdonationList){
                Log.d("Ngo Activity",donation.toString());
            }
        }


    }
}

