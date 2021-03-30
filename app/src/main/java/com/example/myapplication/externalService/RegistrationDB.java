package com.example.myapplication.externalService;

public class RegistrationDB {
    String fullName,phoneNo,dob,password;

    public RegistrationDB() { }

    public RegistrationDB(String fullName, String phoneNo, String dob, String passwrd) {
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.password = passwrd;
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

    public String getPasswrd() {
        return password;
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

    public void setPasswrd(String passwrd) {
        this.password = passwrd;
    }
}
