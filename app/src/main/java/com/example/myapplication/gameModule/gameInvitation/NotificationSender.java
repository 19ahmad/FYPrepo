package com.example.myapplication.gameModule.gameInvitation;

import com.example.myapplication.gameModule.gameInvitation.NotificationData;

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
