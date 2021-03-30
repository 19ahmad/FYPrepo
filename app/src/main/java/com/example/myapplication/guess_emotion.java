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

public class guess_emotion extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    EditText editText;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_emotion);

        Button stopButton = (Button) findViewById(R.id.stopBtn);
        Button quitButton = (Button) findViewById(R.id.quitBtn);
        Button submit = (Button) findViewById(R.id.submitBtn);
        editText = (EditText) findViewById(R.id.guessEmEt);
        textView = (TextView) findViewById(R.id.time);

        stopButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        submit.setOnClickListener(this);

        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished)
            {
               textView.setText(millisUntilFinished/1000 + "sec");
            }

            @Override
            public void onFinish() {
               if(editText.getText().toString().isEmpty())
               {
                   showLoseDialogue();
               }
               else
               {
                   showWinDialogue();
               }
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.stopBtn:
                showStopMenuDialogue();
                break;

            case R.id.quitBtn:
                Intent in2 = new Intent(this, main_Menu.class);
                startActivity(in2);
                countDownTimer.cancel();
                break;
            case R.id.submit:
                if(editText.getText().toString().isEmpty())
                {
                    editText.setError("Input Required");
                    //showLoseDialogue();
                }
                else
                {
                    showWinDialogue();
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
    }
}
