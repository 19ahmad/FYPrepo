package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText dob,username,password,phoneNumber,fullName;
    private RadioButton gendermale,genderfemale;
    private Button signup_button;
    private TextView gender;
    private CountryCodePicker _ccps;
    String userID,_gender,fullNumber;
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    DatePickerDialog.OnDateSetListener setDateListner;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dob = (EditText) findViewById(R.id.DOB);
        username = (EditText) findViewById(R.id.userName);
        password= (EditText) findViewById(R.id.password);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        fullName = (EditText) findViewById(R.id.fullName);
        gendermale =  findViewById(R.id.genderMale);
        genderfemale =  findViewById(R.id.genderFeMale);
        signup_button = (Button) findViewById(R.id.button_signup);
        gender = (TextView) findViewById(R.id.gender);
        _ccps = findViewById(R.id.ccp);

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
                if (!validateFullName() | !validateGender() | !validateOfBirth() | !validatePassword() | !validateUserName() | !validatePhoneNumber()) {
                    Intent intent = new Intent(SignUp.this, main_Menu.class);
                    startActivity(intent);
                } else {
                    String _fullName = fullName.getText().toString().trim();
                    String _phoneNumber = phoneNumber.getText().toString().trim();
                    //fullNumber = _ccps.getFullNumber() + _phoneNumber;
                    String _dob = dob.getText().toString().trim();
                    String _password = password.getText().toString().trim();
                    String _username = username.getText().toString().trim();
                    if (gendermale.isChecked()) {
                        _gender = "Male";

                    }
                    if (genderfemale.isChecked()) {
                        _gender = "Feamle";
                    }

                    //userID = fAuth.getCurrentUser().getUid();
                    DocumentReference df = fireStore.collection("players").document(_phoneNumber);
                    Map<String, Object> user = new HashMap<>();
                    user.put("fName", _fullName);
                    user.put("phoneNumber", _phoneNumber);
                    user.put("username", _username);
                    user.put("DOB", _dob);
                    user.put("password", _password);
                    user.put("gender", _gender);


                    df.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUp.this, "User account is created", Toast.LENGTH_LONG).show();

                        }
                    });

               /* Intent intent = new Intent(SignUp.this, login.class);
                startActivity(intent);
                finish();*/
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
        if(!gendermale.isChecked()| !genderfemale.isChecked())
        {
           // gender.setTextColor(Integer.parseInt("#ff0000"));
            gender.requestFocus();
            return false;

        }
        else
            return true;

    }
}