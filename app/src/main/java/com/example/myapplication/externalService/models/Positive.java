package com.example.myapplication.externalService.models;

import java.io.Serializable;

public class Positive implements Serializable
{
    public int count;
    public Positive(){  count = 0;  }

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Negative{" +
                "count=" + count +
                '}';
    }

    public void setCount(int count) {
        this.count = count;
    }

}
