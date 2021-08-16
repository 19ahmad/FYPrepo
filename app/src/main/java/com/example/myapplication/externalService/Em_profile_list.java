package com.example.myapplication.externalService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.Ep_adapter_1;
import com.example.myapplication.adapters.Ep_adapter_2;
import com.example.myapplication.externalService.models.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Em_profile_list extends AppCompatActivity implements Ep_adapter_1.ItemClickListener {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("chats");
    DatabaseReference mDatabaseEmotional = FirebaseDatabase.getInstance().getReference("emotionalProfile");

    int chatNumber;
    RecyclerView em_profile_list;
    ArrayList<String> messages  = new ArrayList<>();
    ArrayList<String> senders = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> neutral = new ArrayList<>();
    ArrayList<String> assertion = new ArrayList<>();
    final List<Message> chatsWithEmotion = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_profile_list);
        this.setTitle("Emotional Profile");

        Intent intent = getIntent();
        chatNumber = intent.getIntExtra("POSITION",chatNumber);

        em_profile_list = findViewById(R.id.em_profile_list);
       em_profile_list.setLayoutManager(new LinearLayoutManager(this));

        final Ep_adapter_1[] adapter = new Ep_adapter_1[1];

       mDatabase.child(""+(chatNumber+1)).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for (DataSnapshot ds: snapshot.getChildren())
               {
                   String message = ds.child("message").getValue(String.class);
                   String sender = ds.child("sender").getValue(String.class);
                   messages.add(message);
                   senders.add(sender);

               }


//               for(int i =1; i<=messages.size();i++){
                   mDatabaseEmotional.child(""+(chatNumber+1)).addListenerForSingleValueEvent(new ValueEventListener() {
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
                               negative.add(chatsWithEmotion.get(i).getPolarity().getNeutral().getCount());
                               positive.add(chatsWithEmotion.get(i).getPolarity().getNeutral().getCount());
                               neutral.add(chatsWithEmotion.get(i).getPolarity().getNeutral().getCount());

                           }
                           adapter[0] = new Ep_adapter_1(negative,positive,neutral,messages,senders,Em_profile_list.this);
                           em_profile_list.setAdapter(adapter[0]);
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
//               }


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });




       // String _array[] = { "Anees","Raheel","Nadeem","Tabib","Kaleem","Faizan","Talah","Ahmad","Ehtisham","Hamza","Manan","Umer","Talah","Ahmad","Ehtisham","Hamza","Manan","Umer","Talah","Ahmad","Ehtisham","Hamza","Manan","Umer"};
        //em_profile_list.setAdapter(new es_adapter(_array));
    }

    @Override
    public void onItemClick(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Hello world", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Emotional_profile.class);
                intent.putExtra("POSITION",position+1);
                intent.putExtra("CHAT_NUMBER", (chatNumber+1));
                intent.putExtra("MESSAGE",messages.get(position));
                startActivity(intent);
            }
        });
    }
}