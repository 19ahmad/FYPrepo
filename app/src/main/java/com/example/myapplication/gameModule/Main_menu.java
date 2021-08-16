package com.example.myapplication.gameModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.gameModule.gameInvitation.Match_making;
import com.google.firebase.auth.FirebaseAuth;

public class Main_menu extends AppCompatActivity implements View.OnClickListener {

    String _phoneNumber = "";
    ActionBar actionBar;

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // app icon in action bar clicked; go home
//                Intent intent = new Intent(this, Home_activity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menue);

        this.setTitle("Main Menu");



        CardView b1 = (CardView) findViewById(R.id.playTrial);
        CardView b2 = (CardView) findViewById(R.id.startNewGame);
        CardView b3 = (CardView) findViewById(R.id.playeProfile);
        CardView b4 = (CardView) findViewById(R.id.logOut);
        CardView b5 = (CardView) findViewById(R.id.homeActivity);


        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);

  //      Intent i = getIntent();
  //        _phoneNumber = i.getExtras().getString("PHONENUMBER");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.homeActivity:
                Intent intent = new Intent(getApplicationContext(),Home_activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.playTrial:

            case R.id.startNewGame:
                Intent in = new Intent(getApplicationContext(), Guess_polarity.class);
                startActivity(in);
                break;

            case R.id.playeProfile:
                Intent in3 = new Intent(Main_menu.this, Player_profile.class);
                in3.putExtra("PHONENUMBER",_phoneNumber);
                startActivity(in3);
                finish();
                break;

            case R.id.logOut:

//                SharedPreferences sharedPreferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("remember", "false");
//                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent in4 = new Intent(Main_menu.this, Game_login.class);
                startActivity(in4);
                break;

        }

    }
}
