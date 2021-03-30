package com.example.myapplication.externalService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class wUserLogin extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private EditText phoneNumber,password;
    private Button login;
    private TextView signUp;
    private CountryCodePicker _ccpl;
    String fullNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_user_login);

        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.sign_Up);
        _ccpl  =findViewById(R.id.ccp);

        //fullNumber = _ccpl.getFullNumber().toString() + phoneNumber.getText();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(wUserLogin.this,wUserSignup.class);
                startActivity(intent1);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber() | !validatePassword()) {
                    Intent intent2 = new Intent(wUserLogin.this,importChat.class);
                    startActivity(intent2);
                    return;
                } else {
                    isUser();
                    //Intent intent = new Intent(MainActivity.this, profiles_list.class);
                    //startActivity(intent);
                    //finish();
                    //}
                }
            }
        });
    }

    private void isUser()
    {
       // final String _phoneNumber = phoneNumber.getText().toString().trim();
        final String _password = password.getText().toString().trim();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("users");

        // Query authUser = databaseReference.orderByChild("phoneNumber").equalTo(_phoneNumber);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt: dataSnapshot.getChildren()){
                    if(dt.child("phoneNo").getValue().toString().equals(fullNumber)){
                        //Toast.makeText(MainActivity.this,"Phone No Exits, in checking!!", Toast.LENGTH_LONG).show();

                        String passwordFromDB = dt.child("passwrd").getValue().toString();
                        if(passwordFromDB.equals(_password))
                        {
                            Toast.makeText(wUserLogin.this,"Login Successfully", Toast.LENGTH_LONG).show();
                            phoneNumber.setError(null);
                            Intent intent2 = new Intent(wUserLogin.this,em_profile_list.class);
                            startActivity(intent2);

                        }
                        else{
                            Toast.makeText(wUserLogin.this,"Wrong Password", Toast.LENGTH_LONG).show();
                            password.setError("Incorrect Password");
                            password.requestFocus();
                        }

                    }
                    else{
                        Toast.makeText(wUserLogin.this,"No Such User Found", Toast.LENGTH_LONG).show();
                        phoneNumber.setError("Incorrect Phone Number");
                        phoneNumber.requestFocus();
                    }
                }

                /*if(dataSnapshot.exists()) {
                     phoneNumber.setError(null);

                     String passwordFromDB = dataSnapshot.child(_phoneNumber).child("password").getValue(String.class);
                     if(passwordFromDB.equals(_password))
                     {
                         phoneNumber.setError(null);
                         Intent intent2 = new Intent(MainActivity.this,profiles_list.class);
                         startActivity(intent2);
                         finish();
                     }
                     else{
                         password.setError("Wrong Password");
                         password.requestFocus();
                     }
                 }else{
                     phoneNumber.setError("No Such User Found");
                     phoneNumber.requestFocus();
                 }*/
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