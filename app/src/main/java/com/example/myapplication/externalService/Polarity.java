package com.example.myapplication.externalService;

import com.example.myapplication.externalService.models.Negative;
import com.example.myapplication.externalService.models.Neutral;
import com.example.myapplication.externalService.models.Positive;

import java.io.Serializable;

public class Polarity implements Serializable {

    public Positive positive;
    public Neutral neutral;
    public Negative negative;

    public Polarity() {
        this.positive = new Positive();
        this.neutral = new Neutral();
        this.negative = new Negative();
    }


    public Positive getPositive() {
        return positive;
    }

    public void setPositive(Positive positive) {
        this.positive = positive;
    }

    public Neutral getNeutral() {
        return neutral;
    }

    public void setNeutral(Neutral neutral) {
        this.neutral = neutral;
    }

    public Negative getNegative() {
        return negative;
    }

    public void setNegative(Negative negative) {
        this.negative = negative;
    }

    @Override
    public String toString() {
        return "Polarity{" +
                "positive=" + positive.toString() +
                ", neutral=" + neutral.toString() +
                ", negative=" + negative.toString() +
                '}';
    }
}







