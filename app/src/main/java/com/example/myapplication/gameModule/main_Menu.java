package com.example.myapplication.gameModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.google.android.gms.actions.ReserveIntents;
import com.google.firebase.auth.FirebaseAuth;

public class main_Menu extends AppCompatActivity implements View.OnClickListener {

    String _phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menue);
        this.setTitle("Home");

        CardView b1 = (CardView) findViewById(R.id.playTrial);
        CardView b2 = (CardView) findViewById(R.id.startNewGame);
        CardView b3 = (CardView) findViewById(R.id.playeProfile);
        CardView b4 = (CardView) findViewById(R.id.logOut);


        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);

  //      Intent i = getIntent();
  //        _phoneNumber = i.getExtras().getString("PHONENUMBER");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.playTrial:

            case R.id.startNewGame:
                Intent in = new Intent(getApplicationContext(), guess_topic_of_discussion.class);
                startActivity(in);
                break;

            case R.id.playeProfile:
                Intent in3 = new Intent(main_Menu.this, player_profile.class);
                in3.putExtra("PHONENUMBER",_phoneNumber);
                startActivity(in3);
                finish();
                break;

            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Intent in4 = new Intent(main_Menu.this, login.class);
                startActivity(in4);
                break;
        }

    }
}
