package com.example.myapplication.gameModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.externalService.User_login;

public class Home_activity extends AppCompatActivity {
    private Button playerLogin,wUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // actionBar.setTitle(Html.fromHtml("<font color='#fffff'>ActionBartitle </font>"));

        playerLogin = findViewById(R.id.playerLogin);
        wUserLogin = findViewById(R.id.userLogin);

        playerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(Home_activity.this, Game_login.class);
                //Toast.makeText(homeActivity.this, "No user is siggned in", Toast.LENGTH_SHORT).show();
                startActivity(intent2);



            }
        });
        wUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Home_activity.this, User_login.class);
                startActivity(intent2);

            }
        });
    }
}