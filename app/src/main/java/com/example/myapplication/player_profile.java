package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class player_profile extends AppCompatActivity {

    private ImageView back;
    //private Button friendsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        back = findViewById(R.id.goBack);
        //friendsList = findViewById(R.id.friends);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(player_profile.this, main_Menu.class);
                startActivity(intent1);
            }
        });
//        friendsList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(player_profile.this, friendsList.class);
//                startActivity(intent2);
//                finish();
//            }
//        });

    }
}
