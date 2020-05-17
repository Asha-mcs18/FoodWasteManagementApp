package com.example.googlemapsdonor.utils;


import java.util.Random;

public class RandomNumber {
    private static int min = 1000;
    private static int max = 9999;

    public static int getRandomNumber(){
        int randNum =  new Random().nextInt((max-min)+1) +min;
        return randNum;
    }
}
