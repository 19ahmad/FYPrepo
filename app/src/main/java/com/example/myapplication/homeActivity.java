package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.externalService.wUserLogin;

public class homeActivity extends AppCompatActivity {
    private Button playerLogin,wUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        playerLogin = findViewById(R.id.playerLogin);
        wUserLogin = findViewById(R.id.userLogin);

        playerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(homeActivity.this, login.class);
                startActivity(intent1);

            }
        });
        wUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(homeActivity.this, wUserLogin.class);
                startActivity(intent2);

            }
        });
    }
}