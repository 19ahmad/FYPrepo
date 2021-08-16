package com.example.myapplication.gameModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.myapplication.R;
import com.example.myapplication.externalService.Emotional_profile_model;
import com.example.myapplication.externalService.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Guess_emotion extends AppCompatActivity implements View.OnClickListener {


    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("emotionalProfile");
    DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference("polarity");
    DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("players");

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    private boolean isPaused = false;
    private boolean isCanceled = false;
    private long timeRemaining = 0;

    long millisInFuture = 60000;        //30 seconds
    long countDownInterval = 1000;      //1 second


    TextView textView,messageTv,polarityTv;
    EditText editText;
    CountDownTimer countDownTimer;
    String polarity,message,sender,time,date;
    int messageNumber,chatNumber,score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //enable full screen
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_guess_emotion);


        Intent intent = getIntent();
        polarity = intent.getStringExtra("POLARITY");
        message = intent.getStringExtra("MESSAGE");
        chatNumber = intent.getIntExtra("CHAT_NUMBER",chatNumber);
        messageNumber = intent.getIntExtra("MESSAGE_NUMBER",messageNumber);
        sender = intent.getStringExtra("SENDER");
        time = intent.getStringExtra("TIME");
        date = intent.getStringExtra("DATE");
        Log.e("CHAT_NUMBER",""+chatNumber);

        Button stopButton = (Button) findViewById(R.id.stopBtn);
        Button quitButton = (Button) findViewById(R.id.quitBtn);
        Button submit = (Button) findViewById(R.id.submitBtn);
        editText = (EditText) findViewById(R.id.guessEmEt);
        textView = (TextView) findViewById(R.id.time);
        messageTv = (TextView) findViewById(R.id.message1);
        polarityTv = (TextView) findViewById(R.id.polarity);

        messageTv.setText(message);
        polarityTv.setText(polarity);

        stopButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        submit.setOnClickListener(this);

        countDownTimer = new CountDownTimer(millisInFuture,countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished)
            {
                if(isPaused || isCanceled)
                {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    cancel();
                }
                else
                {
                    textView.setText( millisUntilFinished/1000 + "sec");
                    timeRemaining = millisUntilFinished;
                }
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
                isPaused = true;
                showStopMenuDialogue();
                break;

            case R.id.quitBtn:
                isCanceled = true;
                Intent in2 = new Intent(this, Main_menu.class);
                startActivity(in2);
                break;
            case R.id.submitBtn:
                if(editText.getText().toString().isEmpty())
                {
                    editText.setError("Input Required");
                    //showLoseDialogue();
                }
                else
                {
                    isCanceled = true;


                    mDatabase2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Player_registration player_registration = snapshot.getValue(Player_registration.class);
                            if(player_registration!= null)
                            {
                                score = player_registration.getScore();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    mDatabase2.child(userID).child("score").setValue(score+1);

                    final String emotionalAssertion = editText.getText().toString();
                    //Emotional_profile_model emotional_profile_model = new Emotional_profile_model(sender,time,date,message);


                    final ArrayList<Message> list = new ArrayList<>();
                    final Message[] message = new Message[1];
//                    mDatabase.child(""+chatNumber).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            int count = 0;
//                            for (DataSnapshot ds: snapshot.getChildren())
//                            {
//                                Message message1 = ds.getValue(Message.class);
//                                list.add(message1);
//                                count++;
//                                Log.e("#############################################", "onDataChange: "+message1.toString());
//                            }
//                            if(messageNumber <= count) {
//                                message[0] = list.get(messageNumber);
//
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });



                    mDatabase.child(""+chatNumber).child(""+messageNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                // user exists in the database
                                Toast.makeText(getApplicationContext(),""+dataSnapshot.child("emotionalAssertion").getValue(Message.class).toString(),Toast.LENGTH_SHORT);
                                message[0] = dataSnapshot.child("emotionalAssertion").getValue(Message.class);
                                message[0].getFeedback().add(emotionalAssertion);
                                if(polarityTv.getText().toString().equals("Negative"))
                                {
                                    message[0].getPolarity().getNegative().increment();
                                }
                                else if(polarityTv.getText().toString().equals("Positive"))
                                {
                                    message[0].getPolarity().getPositive().increment();
                                }
                                else if(polarityTv.getText().toString().equals("Neutral"))
                                {
                                    message[0].getPolarity().getNeutral().increment();
                                }
                                dataSnapshot.child("emotionalAssertion").getRef().setValue(message[0]);
                                Log.e("############################################3", "onComplete: "+ message[0].toString());
                            }else{
                                // user does not exist in the database
                                Log.e("############################################3", "CHAT DOES NOT EXITS" );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


//                    mDatabase.child(""+chatNumber).child(""+messageNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DataSnapshot> task) {
//                            message[0] = task.getResult().getValue(Message.class);
//                            Log.e("############################################3", "onComplete: "+task.getResult().getValue() );
//                        }
//                    });


//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    // Message message = mDatabase.child(""+chatNumber).child(""+messageNumber).child("emotionalAssertion").gegetValue(Message.class);

//                    if(message[0] == null)
//                    {
//                        Log.e("###########################", "onClick: MESSAGE IS NULL");
//                        message[0] = new Message();
//                    }



                    //mDatabase.child(""+chatNumber).child(""+messageNumber).child("emotionalAssertion").setValue(message[0]);
                    showWinDialogue();
                   // mDatabase.child(""+chatNumber).child(""+messageNumber).child("emotionalAssertion").setValue(emotionalAssertion);
//                    mDatabase.child(""+chatNumber).child(""+messageNumber).child(""+emotionalAssertion).
//                    mDatabase.child(""+chatNumber).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(polarityTv.getText().toString().equals("negative"))
//                            {
//
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                    //mDatabase.child(""+chatNumber).child(""+messageNumber).child("assertion").push().setValue(emotionalAssertion);


                }
                break;
        }
    }

    public void showLoseDialogue()
    {
        LayoutInflater lInflater = LayoutInflater.from(this);
        View view = lInflater.inflate(R.layout.time_over, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();

        Button playAgain =  view.findViewById(R.id.playagain);
        Button mainMenu =  view.findViewById(R.id.mainmenu);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Guess_polarity.class);
                startActivity(intent1);
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCanceled = true;
                Intent intent2 = new Intent(getApplicationContext(), Main_menu.class);
                startActivity(intent2);
                finish();
            }
        });

    }
    public void showWinDialogue()
    {
        LayoutInflater lInflater = LayoutInflater.from(this);
        View view = lInflater.inflate(R.layout.win_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        Button playAgain =  view.findViewById(R.id.playagainButton);
        Button mainMenu =  view.findViewById(R.id.mainMenuButton);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Guess_polarity.class);
                startActivity(intent1);
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCanceled = true;
                Intent intent2 = new Intent(getApplicationContext(), Main_menu.class);
                startActivity(intent2);
                finish();
            }
        });
        //alertDialog.dismiss();
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
                isPaused = false;
                isCanceled = false;

                long millisInFuture = timeRemaining;
                long countDownInterval = 1000;
                new CountDownTimer(millisInFuture,countDownInterval)
                {

                    @Override
                    public void onTick(long millisUntilFinished)
                    {
                        if(isPaused || isCanceled)
                        {
                            //If the user request to cancel or paused the
                            //CountDownTimer we will cancel the current instance
                            cancel();
                        }
                        else
                        {
                            textView.setText( millisUntilFinished/1000 + "sec");
                            timeRemaining = millisUntilFinished;
                        }

                    }

                    @Override
                    public void onFinish()
                    {
                        if(editText.getText().toString().isEmpty())
                        {
                            showLoseDialogue();
                        }
                        else
                        {
                            showWinDialogue();
                        }
                    }
                }.start();

                //startActivity(intent3);
            }
        });
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCanceled = true;
                Intent intent4 = new Intent(getApplicationContext(), Main_menu.class);
                Toast.makeText(getApplicationContext(), "Game Finished", Toast.LENGTH_SHORT).show();
                startActivity(intent4);
            }
        });
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCanceled = true;
                Intent intent5 = new Intent(getApplicationContext(), Main_menu.class);
                startActivity(intent5);
            }
        });

    }
}
