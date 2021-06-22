package com.example.myapplication.gameModule;

public class NotificationData {
    private String Title = "";
    private String Body = "";

    public NotificationData(String title, String body) {
        Title = title;
        Body = body;
    }

    public NotificationData() {
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
