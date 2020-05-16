package com.example.googlemapsdonor.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserModel {
    private String userKey;
    private String userName;
    private String password;
    private String role;
    private String mobileNo;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public UserModel(String userName,String role) {
        this.userName = userName;
        //this.password = password;
        this.role = role;
    }

    public UserModel(String userKey, String userName, String role, String mobileNo) {
        this.userKey = userKey;
        this.userName = userName;
        this.role = role;
        this.mobileNo = mobileNo;
    }

    public UserModel() {
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isValid(){
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher =  pattern.matcher(this.getUserName());
        if(this.role.equals("")/*||this.getPassword().equals("")*/||this.getMobileNo().length()<10||!matcher.matches()){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userKey='" + userKey + '\'' +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}
