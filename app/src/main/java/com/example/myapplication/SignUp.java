package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class SignUp extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private EditText dob,username,password,phoneNumber,fullName;
    private RadioButton gendermale,genderfemale;
    private Button signup_button;
    private TextView gender;
    String userID,_gender;
    //FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    //FirebaseAuth fAuth = FirebaseAuth.getInstance();
    //DatePickerDialog.OnDateSetListener setDateListner;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign up");

        dob = (EditText) findViewById(R.id.DOB);
        username = (EditText) findViewById(R.id.userName);
        password= (EditText) findViewById(R.id.password);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        fullName = (EditText) findViewById(R.id.fullName);
        gendermale =  findViewById(R.id.genderMale);
        genderfemale =  findViewById(R.id.genderFeMale);
        signup_button = (Button) findViewById(R.id.button_signup);
        gender = (TextView) findViewById(R.id.gender);
        //_ccps = findViewById(R.id.ccp);

        Calendar calender =  Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        dob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SignUp.this,  new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dob.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFullName() |  !validateOfBirth() | !validatePassword() | !validateUserName() | !validatePhoneNumber() | !validateGender()) {
                   return;
                }
                else
                    {
                        Toast.makeText(SignUp.this, "User account is created", Toast.LENGTH_LONG).show();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference= firebaseDatabase.getReference();
                        String key = databaseReference.push().getKey();


                        String _fullName = fullName.getText().toString().trim();
                        String _phoneNumber = phoneNumber.getText().toString().trim();
                        String _dob = dob.getText().toString().trim();
                        String _password = password.getText().toString().trim();
                        String _username = username.getText().toString().trim();
                        if (gendermale.isChecked()) {
                            _gender = "Male";

                        }
                        if (genderfemale.isChecked()) {
                            _gender = "Feamle";
                        }
                        final String refreshToken = FirebaseInstanceId.getInstance().getToken();
                        profileRegistration _profileRegistration = new profileRegistration(_fullName,_username,_phoneNumber,_password,_dob,_gender);
                       // updateToken(key);
                        databaseReference.child("Device Tokens").child(_phoneNumber).child("token").setValue(refreshToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("token", refreshToken);
                            }
                        });
                        databaseReference.child("players").child(key).setValue(_profileRegistration).addOnSuccessListener(new OnSuccessListener<Void>() {


                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUp.this, "User account is created", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUp.this, main_Menu.class);
                                startActivity(intent);
                            }

                        });

                    }
            }
        });
    }
    private boolean validateFullName()
    {
        String val = fullName.getText().toString().trim();
        if (val.isEmpty()) {
            fullName.setError("Name Required");
            fullName.requestFocus();
            return false;
        } else {
            fullName.setError(null);
            // fullName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateUserName()
    {
            String val = username.getText().toString().trim();
            if(val.isEmpty())
            {
                username.setError("Username Required");
                username.requestFocus();
                return false;
            }
            else
            {
                username.setError(null);
                // fullName.setErrorEnabled(false);
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
        String val = dob.getText().toString().trim();
        if(val.isEmpty())
        {
            dob.setError("Value Required");
            dob.requestFocus();
            return false;
        }
        else
        {
            dob.setError(null);
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
    private boolean validateGender()
    {
        if(!gendermale.isChecked() && genderfemale.isChecked())
        {
           // gender.setTextColor(Integer.parseInt("#ff0000"));
            return true;

        }
        else if(gendermale.isChecked() && !genderfemale.isChecked())
        {
            return true;
        }
        else
            {
                gender.requestFocus();
                return false;
            }
    }
    private void updateToken(String key)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("players").child(key).setValue(token);
    }


}
