package com.example.myapplication.gameModule;

public class profileRegistration {

    String  key,dob,email,phoneNumber,fullName,gender;

    //empty constructor
    public profileRegistration(){}



    // constructor with parameters
    public profileRegistration(String fullName, String username, String phoneNumber, String dob, String gender )
    {
        //this.token = token;
        this.fullName = fullName;
        this.email = username;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.gender = gender;

    }
// getter and setter



    public  String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
