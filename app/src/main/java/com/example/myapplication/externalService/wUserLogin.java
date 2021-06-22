package com.example.myapplication.externalService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.gameModule.login;
import com.example.myapplication.gameModule.main_Menu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class wUserLogin extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText esEmail,password;
    private Button login;
    private TextView signUp;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fUser = mAuth.getCurrentUser();
        if(fUser !=null)
        {
            startActivity(new Intent(getApplicationContext(),importChat.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_user_login);
        this.setTitle("Login");



        esEmail = findViewById(R.id.esLoginEmail);
        password = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.sign_Up);
        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(wUserLogin.this,wUserSignup.class);
                startActivity(intent1);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validEmail() | !validatePassword()) {
//                    Intent intent2 = new Intent(wUserLogin.this,importChat.class);
//                    startActivity(intent2);
                    return;
                } else {
                    isUser();

                }
            }
        });
    }

    private void isUser()
    {
       final String _esEmail = esEmail.getText().toString().trim();
       final String _password = password.getText().toString().trim();

       mAuth.signInWithEmailAndPassword(_esEmail, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful())
               {

                   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                   if(user.isEmailVerified())
                   {
                       Intent intent = new Intent(getApplicationContext(), importChat.class);
                       startActivity(intent);
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
//        databaseReference= firebaseDatabase.getReference("users");
//
//        // Query authUser = databaseReference.orderByChild("phoneNumber").equalTo(_phoneNumber);
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dt: dataSnapshot.getChildren()){
//                    if(dt.child("phoneNo").getValue().toString().equals(_esEmail)){
//                        //Toast.makeText(MainActivity.this,"Phone No Exits, in checking!!", Toast.LENGTH_LONG).show();
//
//                        String passwordFromDB = dt.child("passwrd").getValue().toString();
//                        if(passwordFromDB.equals(_password))
//                        {
//                            Toast.makeText(wUserLogin.this,"Login Successfully", Toast.LENGTH_LONG).show();
//                            esEmail.setError(null);
//                            Intent intent2 = new Intent(wUserLogin.this,em_profile_list.class);
//                            intent2.putExtra("number",_esEmail);
//                            startActivity(intent2);
//
//                        }
//                        else{
//                            Toast.makeText(wUserLogin.this,"Wrong Password", Toast.LENGTH_LONG).show();
//                            password.setError("Incorrect Password");
//                            password.requestFocus();
//                        }
//
//                    }
//                    else{
//                        Toast.makeText(wUserLogin.this,"No Such User Found", Toast.LENGTH_LONG).show();
//                        esEmail.setError("Incorrect Phone Number");
//                        esEmail.requestFocus();
//                    }
//                }
//
//                /*if(dataSnapshot.exists()) {
//                     phoneNumber.setError(null);
//
//                     String passwordFromDB = dataSnapshot.child(_phoneNumber).child("password").getValue(String.class);
//                     if(passwordFromDB.equals(_password))
//                     {
//                         phoneNumber.setError(null);
//                         Intent intent2 = new Intent(MainActivity.this,profiles_list.class);
//                         startActivity(intent2);
//                         finish();
//                     }
//                     else{
//                         password.setError("Wrong Password");
//                         password.requestFocus();
//                     }
//                 }else{
//                     phoneNumber.setError("No Such User Found");
//                     phoneNumber.requestFocus();
//                 }*/
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }
    private boolean validEmail()
    {
        String val = esEmail.getText().toString().trim();
        if(val.isEmpty())
        {
            esEmail.setError("Phone Number Required");
            esEmail.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(val).matches())
        {
            esEmail.setError("Enter Valid Email");
            esEmail.requestFocus();
            return false;
        }
        else
        {
            esEmail.setError(null);
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