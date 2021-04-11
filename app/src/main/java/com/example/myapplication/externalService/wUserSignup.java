package com.example.myapplication.externalService;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class wUserSignup extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private EditText name,phoneNumber,DOB,password;
    private Button signUp;
    private TextView signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_user_signup);
        this.setTitle("Sign up");

        name = findViewById(R.id.uname);
        phoneNumber = findViewById(R.id.phoneNumber);
        DOB = findViewById(R.id.dob);
        password = findViewById(R.id.pwd);
        signUp = findViewById(R.id.register);
        signIn = findViewById(R.id.sign_in);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFullName() | !validatePhoneNumber() | !validateOfBirth() | !validatePassword()) {
                    return;
                } else {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference= firebaseDatabase.getReference("users");
                    String key = databaseReference.push().getKey();
                    String _fullName = name.getText().toString().trim();
                    String _phoneNumber = phoneNumber.getText().toString().trim();
                    String _dob = DOB.getText().toString().trim();
                    String _password = password.getText().toString().trim();
                    RegistrationDB registrationDB = new RegistrationDB(_fullName,_phoneNumber,_dob,_password);
                    databaseReference.child(key).setValue(registrationDB);

                    // move to next activity

                    Intent intent1 = new Intent(getApplicationContext(), number_verification.class);
                    intent1.putExtra("phoneNumber",_phoneNumber);
                    startActivity(intent1);
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(wUserSignup.this, wUserLogin .class);
                startActivity(intent2);
            }
        });
    }
    private boolean validateFullName()
    {
        String val = name.getText().toString().trim();
        if(val.isEmpty())
        {
            name.setError("Value Required");
            name.requestFocus();
            return false;
        }
        else
        {
            name.setError(null);
            // name.setErrorEnabled(false);
            return true;
        }
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
    private boolean validateOfBirth()
    {
        String val = name.getText().toString().trim();
        if(val.isEmpty())
        {
            name.setError("Date of Birth Required");
            name.requestFocus();
            return false;
        }
        else
        {
            name.setError(null);
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