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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class importChat extends AppCompatActivity {
    private static  final int READ_REQUEST_CODE = 42;
    private static  final int PERMISSION_REQUEST_STORAGE = 1000;
    ImageView importChat;
    TextView textView;
    String entry;
    BufferedReader br;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_chat);

        importChat = findViewById(R.id.import_chat);
        textView = findViewById(R.id.importChat);

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
        File file = new File(Environment.getExternalStorageState(), chat);
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
                path = path.substring(path.indexOf(":") + 1);
               // path = "file://sdcard/chat.txt";
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                Log.d("PATH",path);
                Log.d("Chat", "Chat Data : " + readChat(path));
            }
        }
    }
}