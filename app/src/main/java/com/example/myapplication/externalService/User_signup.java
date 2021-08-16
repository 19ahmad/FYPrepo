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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_signup extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText name,phoneNumber,DOB,password,email;
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
        email = findViewById(R.id.esEmail);
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
                    final String key = databaseReference.push().getKey();
                    final String _fullName = name.getText().toString().trim();
                    final String _email = email.getText().toString().trim();
                    final String _phoneNumber = phoneNumber.getText().toString().trim();
                    final String _dob = DOB.getText().toString().trim();
                    final String _password = password.getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                User_registration userregistration = new User_registration(_fullName,_phoneNumber,_dob,_email);
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userregistration).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Intent intent1 = new Intent(getApplicationContext(), Number_verification.class);    // move to next activity
                                            intent1.putExtra("phoneNumber",_phoneNumber);
                                            startActivity(intent1);
                                        }
                                        else
                                            {
                                                Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                            }
                                    }
                                });

                            }
                            else
                                {
                                    Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(User_signup.this, User_login.class);
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