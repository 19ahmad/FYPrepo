package com.example.myapplication.externalService;

public class RegistrationDB {
    String fullName,phoneNo,dob,email;

    public RegistrationDB() { }

    public RegistrationDB(String fullName, String phoneNo, String dob, String email) {
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
