package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.externalService.em_profile_list;
import com.example.myapplication.externalService.wUserLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class login extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private Button loginBtn;
    private EditText phoneNumber,password;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Login");

        signup = (TextView) findViewById(R.id.button2);
        loginBtn = (Button) findViewById(R.id.button1);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        password = (EditText) findViewById(R.id.password);
        signup.setOnClickListener(this);
        loginBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button1:
                if(!validatePassword() | !validatePhoneNumber())
                {
                    Intent intent2 = new Intent(login.this, main_Menu.class);
                    startActivity(intent2);
                    return;
                }
                else
                    {
                        isUser();
                }

                break;

            case R.id.button2:
                Intent in2 = new Intent(login.this, SignUp.class);
                startActivity(in2);
                break;
        }
    }
    private void isUser()
    {
        final String _phoneNumber = phoneNumber.getText().toString().trim();
        final String _password = password.getText().toString().trim();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("players");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt: dataSnapshot.getChildren()){
                    if(dt.child("phoneNumber").getValue().toString().equals(_phoneNumber)){
                        //Toast.makeText(MainActivity.this,"Phone No Exits, in checking!!", Toast.LENGTH_LONG).show();

                        String passwordFromDB = dt.child("password").getValue().toString();
                        if(passwordFromDB.equals(_password))
                        {
                            Toast.makeText(login.this,"Login Successfully", Toast.LENGTH_LONG).show();
                            phoneNumber.setError(null);
                            Intent intent2 = new Intent(login.this, player_profile.class);
                            //intent2.putExtra("PHONENUMBER",_phoneNumber);
                            startActivity(intent2);
                            break;

                        }
                        else{
                            Toast.makeText(login.this,"Wrong Password", Toast.LENGTH_LONG).show();
                            password.setError("Incorrect Password");
                            password.requestFocus();
                        }
                        break;

                    }
                    else{
                        Toast.makeText(login.this,"No Such User Found", Toast.LENGTH_LONG).show();
                        phoneNumber.setError("Incorrect Phone Number");
                        phoneNumber.requestFocus();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private boolean validatePhoneNumber()
    {
        String val = phoneNumber.getText().toString().trim();
        if(val.isEmpty())
        {
            phoneNumber.setError("Phone Number Required");
            phoneNumber.requestFocus();
            return false;
        }
        else
        {
            phoneNumber.setError(null);
            return true;
        }
    }
    private boolean validatePassword()
    {
        String val = password.getText().toString().trim();
        String pwdVal = "^" +
                "(?=.*[a-zA-z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if(val.isEmpty())
        {
            password.setError("Password Required");
            password.requestFocus();
            return false;
        }
        else if(!val.matches(pwdVal))
        {
            password.setError("Password is too weak");
            password.requestFocus();
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }
}
