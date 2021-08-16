package com.example.myapplication.externalService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.Ep_adapter_1;
import com.example.myapplication.adapters.Ep_adapter_2;
import com.example.myapplication.externalService.models.Chat;
import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Emotional_profile extends AppCompatActivity {
    TextView tv;
    RecyclerView recyclerView;
    final List<Message> chatsWithEmotion = new ArrayList<>();
    ArrayList<ArrayList> assertions  = new ArrayList<>();
    int messageNumber,chatNumber;
    String message;
    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("emotionalProfile");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotional_profile);

        Intent intent = getIntent();
        chatNumber = intent.getIntExtra("CHAT_NUMBER",chatNumber);
        messageNumber = intent.getIntExtra("POSITION",messageNumber);
        message = intent.getStringExtra("MESSAGE");
        this.setTitle(message);
        recyclerView = findViewById(R.id.assertionlist);
        tv = findViewById(R.id.empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Ep_adapter_2[] adapter = new Ep_adapter_2[1];
        Log.e("MESSAGE NUMBER",""+(messageNumber+1));
        Log.e("CHAT NUMBER:", ""+(chatNumber+1));

        dbReference.child(""+(chatNumber+1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Log.e("Number of messages new", ""+ds.getValue(Chat.class).toString());
                    chatsWithEmotion.add(ds.child("emotionalAssertion").getValue(Message.class));


                }
                Log.e("SIZE#########################################",""+chatsWithEmotion.size());
                for (int i =0; i<chatsWithEmotion.size();i++)
                {
                    //Log.e("SIZE#########################################",""+chatsWithEmotion.get(i).getPolarity().getNeutral().getCount());
                    assertions.add((ArrayList) chatsWithEmotion.get(i).getFeedback());
                    Log.e("Feedback######",chatsWithEmotion.get(i).getFeedback().toString());

                }
               // adapter[0] = new Ep_adapter_2(assertions.get(messageNumber+1));
                //recyclerView.setAdapter(adapter[0]);
//                if((assertions.get(messageNumber)).size()>=0) {
//
//                }
//                else
//                {
//                    tv.setText("Assertions Not Available");
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        dbReference.child(""+(chatNumber+1)).child(""+(messageNumber+1)).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                long assertionCount = snapshot.getChildrenCount();
//                if(assertionCount > 0)
//                {
//                    for (DataSnapshot ds: snapshot.getChildren())
//                    {
//
//                        String assertion = ds.getValue(String.class);
//                        Log.e("ASSERTION",assertion);
//                        assertions.add(assertion);
//                    }
//                    adapter[0] = new Ep_adapter_2(assertions);
//                    recyclerView.setAdapter(adapter[0]);
//                }
//                else
//                {
//                    tv.setText("Assertions Not Available");
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }
}