package com.example.myapplication.gameModule.gameInvitation;

public class Notification_data {
    private String Title = "";
    private String Body = "";

    public Notification_data(String title, String body) {
        Title = title;
        Body = body;
    }

    public Notification_data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }
}
