package com.example.myapplication.gameModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.externalService.Upload_chat;
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
import java.util.HashMap;
import java.util.List;

public class Guess_polarity extends AppCompatActivity implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    String path;
    String message,sender,time,date;
    int chatNumber,messageNumber;



    CountDownTimer countDownTimer;
    TextView textView,msg;
    EditText editText;
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("chats");
    DatabaseReference playerReference = FirebaseDatabase.getInstance().getReference("players");

    private boolean isPaused = false;
    private boolean isCanceled = false;
    private long timeRemaining = 0;

    private static final String[] COUNTRIES = new String[] {
            "Negative", "Positive", "Neutral"
    };

    long millisInFuture = 60000;        //30 seconds
    long countDownInterval = 1000;      //1 second


//    @Override
//    protected void onStart() {
//        super.onStart();
////        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                for (final DataSnapshot ds: snapshot.getChildren())
////                {
////                    final String chatNumber = ds.getChildren().iterator().next().getKey();   // GET CHAT NUMBER TO MATCH PLAYER CHAT STATE
////                    final String messageNumber =  ds.getChildren().iterator().next().getChildren().iterator().next().getChildren().iterator().next().getKey();
////                    Log.e("Chat Number ", chatNumber);
////                    Log.e("Message Number", messageNumber);
////                    playerReference.addListenerForSingleValueEvent(new ValueEventListener() { // VALUE LISTENER TO GET PLAYER CHAT STATE
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot snapshot) {
////                           Player_registration player_registration = snapshot.getValue(Player_registration.class);
////                           Log.e("Player at chat", ""+(player_registration.getAtChat()+1));
////                           if (chatNumber.equals(""+(player_registration.getAtChat()+1)))
////                           {
////                               if(messageNumber.equals(""+(player_registration.getAtMessage()+1))) {
////                                   Upload_chat messages = ds.getChildren().iterator().next().getChildren().iterator().next().getChildren().iterator().next().getValue(Upload_chat.class);
////                                   if (messages != null) {
////                                       Log.e("messgaes",""+messages.getMsg());
////                                       HashMap<String, String> message = messages.getMsg();
////                                       if (message!=null) {
////                                           String msg = message.get("message");
////                                           Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
////                                       } else {
////                                           Toast.makeText(getApplicationContext(), "There is no message", Toast.LENGTH_SHORT).show();
////                                       }
////
////                                   }
////                                   else
////                                   {
////                                       Toast.makeText(getApplicationContext(), "Null Value", Toast.LENGTH_SHORT).show();
////                                   }
////                               }
////                               // msg.setText("Hello world");
////                           }
////                           else
////                           {
////                               Toast.makeText(getApplicationContext(), "Bye World", Toast.LENGTH_SHORT).show();
////                           }
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError error) {
////
////                            Toast.makeText(getApplicationContext(), "Something went wrong 1", Toast.LENGTH_SHORT).show();
////                        }
////                    });
////                }
////
////                //Toast.makeText(getApplicationContext(), ""+snapshot.getValue(), Toast.LENGTH_SHORT).show();
////               // Log.e("data", ""+snapshot.getValue());
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////                Toast.makeText(getApplicationContext(), "Something went wrong 2", Toast.LENGTH_SHORT).show();
////            }
////        });
//    } // END OF ON START

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //enable full screen
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guess_polarity);


        Button stpButton = (Button) findViewById(R.id.stop);
        Button quitButton = (Button) findViewById(R.id.quit);
        Button ok = (Button) findViewById(R.id.submit);
        textView = (TextView) findViewById(R.id.time1);
        msg = (TextView) findViewById(R.id.message);
        //editText = (EditText) findViewById(R.id.emotionalState);

        AutoCompleteTextView polarity = (AutoCompleteTextView) findViewById(R.id.polarity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        polarity.setAdapter(adapter);


        // GET MESSAGES FROM CHAT AND DISPLAY IN FRONT OF PLAYER SO HE CAN GUESS THE POLARITY OF MESSAGE
        playerReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Player_registration player_registration = snapshot.getValue(Player_registration.class);
                messageNumber = player_registration.getAtMessage();
                chatNumber = player_registration.getAtChat();
//                Log.e("Message Number",""+(messageNumber+1));
//                Log.e("Chat Number",""+(chatNumber+2));
                path = "/chats/"+(chatNumber+1)+"/"+(messageNumber+1);
                Log.e("PATH",path);
                userReference.child(""+(chatNumber+1)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long numberOfMessages = snapshot.getChildrenCount();
                        Log.e("Number of Messages", ""+numberOfMessages);
                        if(messageNumber < numberOfMessages-1)
                        {
                            List<String> senders = new ArrayList<>();
                            List<String> dates = new ArrayList<>();
                            List<String> timeList = new ArrayList<>();
                            List<String> chat = new ArrayList<>();
                            for (DataSnapshot ds: snapshot.getChildren())
                            {
                                String _message = ds.child("message").getValue(String.class);
                                String _time = ds.child("time").getValue(String.class);
                                String _date = ds.child("date").getValue(String.class);
                                String _sender = ds.child("sender").getValue(String.class);
                                chat.add(_message);
                                senders.add(_sender);
                                timeList.add(_time);
                                dates.add(_date);
                                Log.e("Message" , ""+_message);
                            }
                            time = timeList.get(messageNumber);
                            date = dates.get(messageNumber);
                            sender = senders.get(messageNumber);
                            msg.setText(chat.get(messageNumber));
                            playerReference.child(userID).child("atMessage").setValue(messageNumber+1);
                        }
                        else
                        {
                            playerReference.child(userID).child("atChat").setValue(chatNumber+1);
                            playerReference.child(userID).child("atMessage").setValue(0);
                            userReference.child(""+(chatNumber+1)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    List<String> chat = new ArrayList<>();
                                    for (DataSnapshot ds: snapshot.getChildren())
                                    {
                                        String message = ds.child("message").getValue(String.class);
                                        chat.add(message);
                                    }
                                    msg.setText(chat.get(0));
                                    playerReference.child(userID).child("atMessage").setValue(messageNumber+1);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });


        stpButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        ok.setOnClickListener(this);
        ArrayList<DataSnapshot> dt = null;

//            dt = getData(db,"3431401398");
//
//        if(dt != null){
//            Log.d("onCreate", "Children Count : "+dt.size());;
//        }
//        else
//            Log.d("isEmpty", "onCreate: Empty object");


        countDownTimer = new CountDownTimer(millisInFuture,countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {

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
                    Intent intent = new Intent(Guess_polarity.this, Guess_emotion.class);
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
                isPaused = true;
                showStopMenuDialogue();
                //countDownTimer.cancel();
                break;
            case R.id.quit:
                Intent in1 = new Intent(this, Main_menu.class);
                startActivity(in1);
                break;
            case R.id.submit:
                //EditText emEt = (EditText) findViewById(R.id.emotionalState);
                AutoCompleteTextView autoCompleteTextView = findViewById(R.id.polarity);
                if(autoCompleteTextView.getText().toString().isEmpty())
                {
                    autoCompleteTextView.setError("Input Required");
                }
                else
                {
                    isCanceled = true;
                    String polarity = autoCompleteTextView.getText().toString();
                    String message = msg.getText().toString();
                    Intent intent = new Intent(Guess_polarity.this, Guess_emotion.class);
                    intent.putExtra("POLARITY",polarity);
                    intent.putExtra("MESSAGE",message);
                    intent.putExtra("MESSAGE_NUMBER",(messageNumber+1));
                    intent.putExtra("CHAT_NUMBER",(chatNumber+1));
                    intent.putExtra("SENDER",sender);
                    intent.putExtra("DATE",date);
                    intent.putExtra("TIME",time);
                    startActivity(intent);
                }
                break;
        }

    }
    public void showLoseDialogue()
    {
        LayoutInflater lInflater = LayoutInflater.from(this);
        View view = lInflater.inflate(R.layout.time_over, null);
        Button playAgain =  view.findViewById(R.id.playagain);
        Button mainMenu =  view.findViewById(R.id.mainmenu);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent1 = new Intent(getApplicationContext(), Guess_polarity.class);
                startActivity(intent1);
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
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
                            Intent intent = new Intent(Guess_polarity.this, Guess_emotion.class);
                            startActivity(intent);
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
