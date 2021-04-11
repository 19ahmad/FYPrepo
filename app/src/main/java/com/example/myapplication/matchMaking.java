package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class matchMaking extends AppCompatActivity {
    Button inviteFriends,inviteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);
        this.setTitle("Invite Players");
        inviteFriends = findViewById(R.id.iButton1);
        inviteAll = findViewById(R.id.iButton2);


        inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),contactsList.class);
                startActivity(intent1);
            }
        });
        inviteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), contactsList.class);
                startActivity(intent2);
            }
        });
    }

}