package com.example.googlemapsdonor.models;

import java.util.List;//<<<<<<< HEAD
import java.util.List;
import java.util.Map;

public abstract class  DataStatus {
    public  void dataLoaded(Object object){}
    public  void dataLoaded(List<?> donations){}
    public  void dataLoaded(String object){}
    public void dataCreated(Object object){}
    public abstract   void errorOccured(String message);
    public  void dataUpdated(String message){}
    public void dataCreated(String message){}
}
