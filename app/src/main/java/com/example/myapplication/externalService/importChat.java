package com.example.myapplication.externalService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;

public class importChat extends AppCompatActivity {
    private static  final int READ_REQUEST_CODE = 42;
    private static  final int PERMISSION_REQUEST_STORAGE = 1000;
    ImageView importChat;
    TextView textView;
    String entry;
    BufferedReader br;
    DatabaseReference mDatabase;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static ArrayList<HashMap<String,String>> chatData;
    private static int count;
    public String KEY = "DEFAULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_chat);
        this.setTitle("Import Chat");

        importChat = findViewById(R.id.import_chat);
        textView = findViewById(R.id.importChat);
        mDatabase = firebaseDatabase.getReference();
        count = 0;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        importChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectChatFromStorage();
            }
        });

        getData(mDatabase,"3156738688");





    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_STORAGE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission Given.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Permission not Given.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private String readChat(String chat)
    {
        File file = new File( chat);
        StringBuilder msg = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null)
            {
                msg.append(line);
                msg.append("\n");
            }
        }catch (IOException e){e.printStackTrace();}
        return msg.toString();
    }

    private void selectChatFromStorage()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = this.getFilesDir()+"/"+"chat.txt";
               // path = path.substring(path.indexOf(":") + 1);
//                if (path.contains("Emulated"))
//                {
//                    path = path.substring(path.indexOf("0")+1);
//
//                }
               // path = "file://sdcard/chat.txt";
                Toast.makeText(this, "Path is " + path, Toast.LENGTH_SHORT).show();
                Log.d("PATH",path);
                chatData = chatImporter.getData(path);
               // Log.d("Chat", "Chat Data : " + chatImporter.getData(path).toString());

                for (HashMap<String,String> i : chatData){
//                    Log.d("date", " :    "+ i.get("date"));
//                    Log.d("sender", " :    "+ i.get("sender"));
//                    Log.d("time", " :    "+ i.get("time"));
//                    Log.d("message", " :    "+ i.get("message"));
//                    Log.d("-------", "--------------------------------------------------------------------------------------------------------------\n\n\n");
                    Log.d("CHAT ITEMS", "List items " + chatData.size());
                    //uploadData(mDatabase,i,"3156738688");
                    count+=1;
                    Log.d("", "COUNT = "+ count);

                }

            }
        }
    }

    public void uploadData(final DatabaseReference db, HashMap<String,String> msg, final String currentUser){
        final DatabaseReference usr = db.child("users");
        final HashMap<String,String> msg1 = msg;
        //final DatabaseReference[] currentUserRef = new DatabaseReference[1];
//        usr.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                  Log.d("DATA", ""+snapshot.getChildrenCount());
//
//                for(DataSnapshot user : snapshot.getChildren()){
//                    Log.d("DATA", ""+user.child("phoneNo").getValue());
//                    if(user.child("phoneNo").getValue(String.class).equals(currentUser)){
//                        //Log.d("DATA", ""+user.child("phoneNo").getValue());
//                        //currentUserRef[0] = user.getRef();
//                        KEY += user.getKey();
////                        count+=1;
////                        Log.d("Count check", "COUNT IS "+ count);
////                        String key = user.child("chats").getRef().push().getKey();
////                        user.child("chats").child(key).getRef().setValue(msg1);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        String key = usr.child("chats").getRef().push().getKey();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm");
//        String format = s.format(new Date());
//        db.child("chats").child(currentUser).child(format).child(key).getRef().setValue(msg1);



    }
    void getData(DatabaseReference db, String currentUser){
        String p = "/chats/"+currentUser;
        DatabaseReference r = db.child(p).getRef();
        Task<DataSnapshot> d = r.get();
        d.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("GET TIMESTAMP", "DATA: "+task.getResult().getValue());
                }
            }
        });
    }
}