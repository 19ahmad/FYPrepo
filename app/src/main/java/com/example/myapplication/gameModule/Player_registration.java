package com.example.myapplication.gameModule;

public class Player_registration {

    String  dob,email,phoneNumber,fullName,gender;
    int score;
    int atChat,atMessage;

    //empty constructor
    public Player_registration(){}

    // constructor with parameters
    public Player_registration(String fullName, String email, String phoneNumber, String dob, String gender, int atChat, int atMessage,int score)
    {
        //this.token = token;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.gender = gender;
        this.atChat = atChat;
        this.atMessage = atMessage;

    }

    // getter and setter

    public  String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAtChat() {
        return atChat;
    }

    public void setAtChat(int atChat) {
        this.atChat = atChat;
    }

    public int getAtMessage() {
        return atMessage;
    }

    public void setAtMessage(int atMessage) {
        this.atMessage = atMessage;
    }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }
}
