package com.example.myapplication;

public class NotificationSender {

    public NotificationData data;
    public String toUser;

    public NotificationSender(NotificationData data, String toUser)
    {
        this.data = data;
        this.toUser = toUser;
    }

    public NotificationSender() {
    }
}
