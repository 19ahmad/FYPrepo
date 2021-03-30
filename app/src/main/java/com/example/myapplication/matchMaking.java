package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class matchMaking extends AppCompatActivity {
    Button inviteFriends,inviteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);

        inviteFriends = findViewById(R.id.iButton1);
        inviteAll = findViewById(R.id.iButton2);
    }
}