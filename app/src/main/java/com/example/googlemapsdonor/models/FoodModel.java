package com.example.googlemapsdonor.models;

public class FoodModel {
    private String foodItem;
    private int shelfLife;
    private String foodKey;
    private int noOfPersons;

    FoodModel(){

    }

    public FoodModel(String foodItem, int shelfLife, int noOfPersons) {
        this.foodItem = foodItem;
        this.shelfLife = shelfLife;
        this.noOfPersons = noOfPersons;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem= foodItem;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getFoodKey() {
        return foodKey;
    }

    public void setFoodKey(String foodKey) {
        this.foodKey = foodKey;
    }

    public int getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(int noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    @Override
    public String toString() {
        return "FoodModel{" +
                "foodItem='" + foodItem + '\'' +
                ", shelfLife=" + shelfLife +
                ", foodKey='" + foodKey + '\'' +
                ", noOfPersons=" + noOfPersons +
                '}';
    }
}
