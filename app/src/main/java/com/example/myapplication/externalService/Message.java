package com.example.myapplication.externalService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    public List<String> feedback;
    public Polarity polarity;

    public Message(){
        feedback = new ArrayList<>();
        polarity = new Polarity();
    }

    public List<String> getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "Message{" +
                "feedback=" + feedback +
                ", polarity=" + polarity.toString() +
                '}';
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }

    public Polarity getPolarity() {
        return polarity;
    }

    public void setPolarity(Polarity polarity) {
        this.polarity = polarity;
    }
}
