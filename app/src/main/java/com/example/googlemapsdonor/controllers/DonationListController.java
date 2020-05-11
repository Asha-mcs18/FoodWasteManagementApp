package com.example.googlemapsdonor.controllers;

import android.util.Log;

import com.example.googlemapsdonor.firebasehandler.FBDonationHandler;
import com.example.googlemapsdonor.firebasehandler.FBFoodHandler;
import com.example.googlemapsdonor.models.DataStatus;
import com.example.googlemapsdonor.models.DonationListModel;
import com.example.googlemapsdonor.models.DonationModel;
import com.example.googlemapsdonor.models.FoodModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonationListController {
    private FBFoodHandler foodHandler;
    private FBDonationHandler donationHandler;
    private List<DonationListModel> donationList;
    private String foodKey;
    private Map<String,DonationListModel> donationsMap;
    private int size=0,i=0;


    public DonationListController(){
        foodHandler = new FBFoodHandler();
        donationHandler = new FBDonationHandler();
        donationList = new ArrayList<DonationListModel>();
        donationsMap = new HashMap<String, DonationListModel>();
    }

    public void getDonationList(final DataStatus appDataStatus){
        donationHandler.readDonations(appDataStatus);
//        donationHandler.readDonations(new DataStatus() {
////            @Override
////            public void dataLoaded(List<?> objects) {
////                super.dataLoaded(objects);
////                Log.d("DonationListController","Inside Donation List Controller"+objects.size());
////                donationList = (List<DonationListModel>) (List<?>) objects;
////                donationList = (List<?extends DonationListModel>) object;
////
////                for(final DonationListModel donation: donationList) {
////                    foodKey  =donation.getFoodKey();
////                    Log.d("DonationListController","food key is"+foodKey);
////                     foodHandler.getFoodItem(foodKey, new DataStatus() {
////                         @Override
////                         public void dataLoaded(Object object) {
////                             Log.d("DonationListController","Got food model for food");
////                             super.dataLoaded(object);
////                             FoodModel foodModel = (FoodModel) object;
////                             Log.d("DonationListController","the food read is "+foodModel.toString());
////                             donation.setFoodItem(foodModel.getFoodItem());
////                             donation.setNoOfPersons(foodModel.getNoOfPersons());
////                         }
////
////                         @Override
////                         public void errorOccured(String message) {
////                             Log.d("DonationListController",message);
////                             appDataStatus.errorOccured(message);
////                         }
////                     });
////                }
////                Log.d("DonationListCOntroller","Donation List printed");
////                for(Object o:objects){
////                    DonationListModel donationListModel = (DonationListModel) o;
////                    Log.d("DonationListController", donationListModel.toString());
////                }
////                appDataStatus.dataLoaded(objects);
////                appDataStatus.dataLoaded("data loaded");
////            }
//            @Override
//            public void dataLoaded(String object) {
//                super.dataLoaded(object);
//                Log.d("DonationListController","Inside Donation List Controller"+object);
//                appDataStatus.dataLoaded(object);
//            }
//
//            @Override
//            public void dataLoaded(List<?> donations) {
//                i=0;
//                size=0;
//                donationsMap.clear();
//                super.dataLoaded(donations);
//                Log.d("DonationListController","Inside Donation List Controller"+donations.size());
//                donationList = (List<DonationListModel>)(List<?>)donations;
//                size=donationList.size();
//                Log.d("DonationListController","dlc donation list size is"+size);
//                DonationModel don = new DonationListModel();
//
//
//            }
//
//            @Override
//            public void errorOccured(String message) {
//                Log.d("DonationListController",message);
//                appDataStatus.errorOccured(message);
//            }
//        });
     }
}
//for(final DonationModel donation: donations) {
//        Log.d("DonationListController","instanceof key is"+ (don instanceof DonationModel));
//final DonationListModel d = (DonationListModel) donation;
//        foodKey  =donation.getFoodKey();
//        Log.d("DonationListController","food key is"+foodKey);
//        foodHandler.getFoodItem(foodKey, new DataStatus() {
//@Override
//public void dataLoaded(Object object) {
//        super.dataLoaded(object);
//        Log.d("DonationListController", "Got food model for food");
//        FoodModel foodModel = (FoodModel) object;
//        Log.d("DonationListController", "the food read is " + foodModel.toString());
//        DonationListModel donationListModel = new DonationListModel(donation.getDonorKey(),donation.getPickUpLocationKey(),donation.getFoodKey(),foodModel.getFoodItem(),foodModel.getNoOfPersons());
//        //donation.setFoodItem(foodModel.getFoodItem());
//        //donation.setNoOfPersons(foodModel.getNoOfPersons());
//        Log.d("DonationListController", "food key is" + donationListModel.getFoodItem());
//        Log.d("DonationListController", "food key is" + donationListModel.getNoOfPersons());
//        donationsMap.put(donation.getKey(),donationListModel);
//        Log.d("DonationListController", "donation List item added  is" +donationListModel.toString());
//        }
//
//@Override
//public void errorOccured(String message) {
//        Log.d("DonationListController",message);
//        appDataStatus.errorOccured(message);
//        }
//        });
//        }