package com.example.myapplication;

public class MyResponse {
    public  NotificationData data;
    public String to;

    public MyResponse(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }

    public MyResponse() {
    }
}