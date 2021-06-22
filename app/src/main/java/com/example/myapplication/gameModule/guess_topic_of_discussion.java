package com.example.myapplication.gameModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class guess_topic_of_discussion extends AppCompatActivity implements View.OnClickListener {

    CountDownTimer countDownTimer;
    TextView textView,msg;
    EditText editText;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //enable full screen
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guess_topic_of_discussion);

        Button stpButton = (Button) findViewById(R.id.stop);
        Button quitButton = (Button) findViewById(R.id.quit);
        Button ok = (Button) findViewById(R.id.submit);
        textView = (TextView) findViewById(R.id.time1);
        msg = (TextView) findViewById(R.id.message);
        editText = (EditText) findViewById(R.id.emotionalState);
        db = FirebaseDatabase.getInstance().getReference();

        stpButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        ok.setOnClickListener(this);
        ArrayList<DataSnapshot> dt = null;

            dt = getData(db,"3431401398");

        if(dt != null){
            Log.d("onCreate", "Children Count : "+dt.size());;
        }
        else
            Log.d("isEmpty", "onCreate: Empty object");


        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText( millisUntilFinished/1000 + "sec");

            }

            @Override
            public void onFinish() {
                if(editText.getText().toString().isEmpty())
                {
                    showLoseDialogue();
                }
                else
                {
                    Intent intent = new Intent(guess_topic_of_discussion.this, guess_emotion.class);
                    startActivity(intent);
                }

            }
        }.start();
       // countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.stop:
                showStopMenuDialogue();

                countDownTimer.cancel();
                break;
            case R.id.quit:
                Intent in1 = new Intent(this, main_Menu.class);
                startActivity(in1);
                break;
            case R.id.submit:
                EditText emEt = (EditText) findViewById(R.id.emotionalState);
                if(emEt.getText().toString().isEmpty())
                {
                    showLoseDialogue();
                }
                else
                {
                    Intent intent = new Intent(guess_topic_of_discussion.this, guess_emotion.class);
                    startActivity(intent);
                }
                break;
        }

    }
    public void showLoseDialogue()
    {
        LayoutInflater lInflater = LayoutInflater.from(this);
        View view = lInflater.inflate(R.layout.lose_dialog, null);
        Button playAgain =  view.findViewById(R.id.playagain);
        Button mainMenu =  view.findViewById(R.id.mainmenu);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), guess_topic_of_discussion.class);
                startActivity(intent1);
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), main_Menu.class);

                startActivity(intent2);
                finish();
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();


    }
    public void showWinDialogue()
    {
        LayoutInflater lInflater = LayoutInflater.from(this);
        View view = lInflater.inflate(R.layout.win_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();

    }
    public void showStopMenuDialogue()
    {
        LayoutInflater lInflater = LayoutInflater.from(this);
        View view = lInflater.inflate(R.layout.activity_stop_menu, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        Button resumeBtn = (Button) alertDialog.findViewById(R.id.resume);
        Button quitBtn = (Button) alertDialog.findViewById(R.id.quitButton);
        final Button mainMenuBtn = (Button) alertDialog.findViewById(R.id.mainMenuButton);

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent3  = new Intent(getApplicationContext(),guess_topic_of_discussion.class);
                alertDialog.hide();
                //startActivity(intent3);
            }
        });
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(),main_Menu.class);
                Toast.makeText(getApplicationContext(), "Game Finished", Toast.LENGTH_SHORT).show();
                startActivity(intent4);
            }
        });
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(getApplicationContext(),main_Menu.class);
                startActivity(intent5);
            }
        });
    }

    ArrayList<DataSnapshot> getData(DatabaseReference db, String currentUser) {
        String p = "/chats/"+currentUser;
        DatabaseReference r = db.child(p).getRef();
        Task<DataSnapshot> d = r.get();
        final ArrayList<DataSnapshot> dt = new ArrayList<DataSnapshot>();
        d.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("GET TIMESTAMP", "DATA: "+task.getResult().getValue());
                    dt.add(task.getResult());
                }
            }
        });

        if(!d.isComplete()){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Log.d("Check size", "getData: Count : "+d.isComplete()+"   "+  dt.size());
        return dt;
    }
}
