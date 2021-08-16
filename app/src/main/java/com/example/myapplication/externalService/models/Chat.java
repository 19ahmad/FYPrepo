package com.example.myapplication.externalService.models;

import com.example.myapplication.externalService.Emotional_profile;
import com.example.myapplication.externalService.Message;

import java.io.Serializable;

public class Chat implements Serializable {
    public Message emotionalAssertion;

    public Chat() {
    }

    @Override
    public String toString() {
        return "Chat{" +
                "emotionalAssertion=" + emotionalAssertion.toString() +
                '}';
    }

    public Message getEmotionalAssertion() {
        return emotionalAssertion;
    }

    public void setEmotionalAssertion(Message emotionalAssertion) {
        this.emotionalAssertion = emotionalAssertion;
    }
}
