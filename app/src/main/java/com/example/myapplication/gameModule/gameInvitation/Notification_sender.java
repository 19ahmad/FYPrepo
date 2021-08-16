package com.example.myapplication.gameModule.gameInvitation;

public class Notification_sender {

    public Notification_data data;
    public String toUser;

    public Notification_sender(Notification_data data, String toUser)
    {
        this.data = data;
        this.toUser = toUser;
    }

    public Notification_sender() {
    }
}
