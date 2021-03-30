package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class main_Menu extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menue);

        Button b1 = (Button) findViewById(R.id.playTrial);
        Button b2 = (Button) findViewById(R.id.startNewGame);
        Button b3 = (Button) findViewById(R.id.playeProfile);
        Button b4 = (Button) findViewById(R.id.logOut);


        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.playTrial:

            case R.id.startNewGame:
                Intent in = new Intent(getApplicationContext(), matchMaking.class);
                startActivity(in);
                break;

            case R.id.playeProfile:
                Intent in3 = new Intent(main_Menu.this, player_profile.class);
                startActivity(in3);
                break;

            case R.id.logOut:
                Intent in4 = new Intent(main_Menu.this, login.class);
                startActivity(in4);
                break;
        }

    }
}
