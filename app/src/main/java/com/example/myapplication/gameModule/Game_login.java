package com.example.myapplication.gameModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.externalService.Import_chat;
import com.example.myapplication.externalService.User_login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Game_login extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final String TAG = "LoginActivity";
    private String mCustomToken;

    private Button loginBtn;
    private EditText email,password;
    private TextView signup;
    private CheckBox checkBox;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fUser  = mAuth.getCurrentUser();
        if(fUser != null)
        {
            startActivity(new Intent(getApplicationContext(), Main_menu.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Login");
       // mAuth = FirebaseAuth.getInstance();

//
//        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
//        String checkbox = sharedPreferences.getString("remember","");
//
//        if (checkbox.equals("true"))
//        {
//            startActivity(new Intent(getApplicationContext(), Import_chat.class));
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
//        }

        signup = (TextView) findViewById(R.id.button2);
        loginBtn = (Button) findViewById(R.id.button1);
        email = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.password);
        checkBox = findViewById(R.id.rememberMe);

        signup.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(buttonView.isChecked())
//                {
//                    SharedPreferences sharedPreferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("remember", "true");
//                    editor.apply();
//                    Toast.makeText(Game_login.this, "Checked", Toast.LENGTH_SHORT).show();
//
//                }
//                else if (!buttonView.isChecked())
//                {
//                    SharedPreferences sharedPreferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("remember", "false");
//                    editor.apply();
//                    Toast.makeText(Game_login.this, "Uncheked", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }


//    private void startSignIn() {
//        // [START sign_in_custom]
//        mAuth.signInWithCustomToken(mCustomToken)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCustomToken:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
//        // [END sign_in_custom]
//    }

//    private void updateUI(FirebaseUser currentUser) {
//    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
            {
                case R.id.button1:
                    if(!validatePassword() | !validateEmail())
                        {
//                            Intent intent2 = new Intent(login.this, main_Menu.class);
//                            startActivity(intent2);
                            return;
                        }
                    else
                        {
                            isUser();
                        }
                        break;
            case R.id.button2:
                Intent in2 = new Intent(Game_login.this, Game_signup.class);
                startActivity(in2);
                break;
            }
    }
    private void isUser()
    {
        final String _email = email.getText().toString().trim();
        final String _password = password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        Intent intent2 = new Intent(Game_login.this, Main_menu.class);
                        startActivity(intent2);
                        return;
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(getApplicationContext(), "Check your email to Verify Your account!", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference= firebaseDatabase.getReference("players");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dt: dataSnapshot.getChildren()){
//                    if(dt.child("phoneNumber").getValue().toString().equals(_phoneNumber)){
//                        //Toast.makeText(MainActivity.this,"Phone No Exits, in checking!!", Toast.LENGTH_LONG).show();
//
//                        String passwordFromDB = dt.child("password").getValue().toString();
//                        if(passwordFromDB.equals(_password))
//                            {
//                                Toast.makeText(login.this,"Login Successfully", Toast.LENGTH_LONG).show();
//                                email.setError(null);
//                                Intent intent2 = new Intent(login.this, player_profile.class);
//                                //intent2.putExtra("PHONENUMBER",_phoneNumber);
//                                startActivity(intent2);
//                                break;
//                            }
//                        else
//                            {
//                                Toast.makeText(login.this,"Wrong Password", Toast.LENGTH_LONG).show();
//                                password.setError("Incorrect Password");
//                                password.requestFocus();
//                            }
//                            break;
//                        }
//                    else
//                        {
//                        Toast.makeText(login.this,"No Such User Found", Toast.LENGTH_LONG).show();
//                        email.setError("Incorrect Phone Number");
//                        email.requestFocus();
//                        }
//                    }
//                }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) { }
//        });

    }
    private boolean validateEmail()
    {
        String val = email.getText().toString().trim();
        if(val.isEmpty())
        {
            email.setError("Emial Required");
            email.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(val).matches())
        {
            email.setError("Enter Valid Email");
            email.requestFocus();
            return false;
        }
        else
        {
            email.setError(null);
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
