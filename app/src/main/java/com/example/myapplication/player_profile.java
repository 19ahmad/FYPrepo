package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class player_profile extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private ImageView back;
    private TextView playernName;
    //private Button friendsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        this.setTitle("Profile");
        back = findViewById(R.id.goBack);
        playernName = findViewById(R.id._playerName);

//        Intent i = getIntent();
//        final String _phoneNumber = i.getExtras().getString("PHONENUMBER");
        //friendsList = findViewById(R.id.friends);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("players");
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
