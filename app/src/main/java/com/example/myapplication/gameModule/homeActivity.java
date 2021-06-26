package com.example.myapplication.gameModule;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.externalService.wUserLogin;
import com.example.myapplication.gameModule.gameInvitation.NotificationData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeActivity extends AppCompatActivity {
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

                Intent intent2 = new Intent(homeActivity.this, login.class);
                //Toast.makeText(homeActivity.this, "No user is siggned in", Toast.LENGTH_SHORT).show();
                startActivity(intent2);



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