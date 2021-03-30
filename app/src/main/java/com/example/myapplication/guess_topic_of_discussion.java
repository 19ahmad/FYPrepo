package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class guess_topic_of_discussion extends AppCompatActivity implements View.OnClickListener {

    CountDownTimer countDownTimer;
    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_topic_of_discussion);

        Button stpButton = (Button) findViewById(R.id.stop);
        Button quitButton = (Button) findViewById(R.id.quit);
        Button ok = (Button) findViewById(R.id.submit);
        textView = (TextView) findViewById(R.id.time1);
        editText = (EditText) findViewById(R.id.emotionalState);

        stpButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        ok.setOnClickListener(this);


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
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        Button resumeBtn = (Button) alertDialog.findViewById(R.id.resume);
        Button quitBtn = (Button) alertDialog.findViewById(R.id.quitButton);
        Button mainMenuBtn = (Button) alertDialog.findViewById(R.id.mainMenuButton);

        resumeBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        mainMenuBtn.setOnClickListener(this);
    }
}
