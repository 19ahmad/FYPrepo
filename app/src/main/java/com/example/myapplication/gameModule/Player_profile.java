package com.example.myapplication.gameModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Player_profile extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseUser user;

    private String userID;


    private Button signOut;
    private TextView playernName,email,phoneNumber,gender,DOB;
    //private Button friendsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        this.setTitle("Profile");
        signOut = findViewById(R.id.pSignOut);


//        Intent i = getIntent();
//        final String _phoneNumber = i.getExtras().getString("PHONENUMBER");
        //friendsList = findViewById(R.id.friends);

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference= firebaseDatabase.getReference("players");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dt:snapshot.getChildren())
//                {
//                    if(dt.child("phoneNumber").getValue().toString().equals(_phoneNumber))
//                    {
//                        String _fullName = dt.child("fullName").getValue().toString();
//                        playernName.setText(_fullName);
//                        break;
//                    }
//                    else continue;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(Player_profile.this, Game_login.class);
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("players");
        userID  = user.getUid();

        final TextView name = (TextView) findViewById(R.id.pName);
        final TextView email = (TextView) findViewById(R.id.pEmail);
        final TextView phoneNumber = (TextView) findViewById(R.id.pContact);
        final TextView gender = (TextView) findViewById(R.id.pGender);
        final TextView dob = (TextView) findViewById(R.id.pDOB);
        final TextView score = findViewById(R.id.score);
        final TextView Level = findViewById(R.id.level);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Player_registration userProfile = snapshot.getValue(Player_registration.class);

                if(userProfile != null)
                {
                    String playerName = userProfile.fullName;
                    String playerEmail = userProfile.email;
                    String playerContact = userProfile.phoneNumber;
                    String playerGender = userProfile.gender;
                    String playerDOB = userProfile.dob;
                    int _score = userProfile.getScore();

                    name.setText(playerName);
                    email.setText(playerEmail);
                    phoneNumber.setText(playerContact);
                    gender.setText(playerGender);
                    dob.setText(playerDOB);
                    score.setText(String.valueOf(_score));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getApplicationContext(), "Something Wrong Happened", Toast.LENGTH_LONG).show();
            }
        });


    }
}
