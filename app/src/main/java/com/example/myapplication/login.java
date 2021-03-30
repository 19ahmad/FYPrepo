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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class login extends AppCompatActivity implements View.OnClickListener {

    private Button loginBtn;
    String dbPhoneNumber,dbPassword,fullNumber;
    private EditText phoneNumber,password;
    private TextView signup;
    private CountryCodePicker codePicker;
    FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = (TextView) findViewById(R.id.button2);
        loginBtn = (Button) findViewById(R.id.button1);
        codePicker  =findViewById(R.id.ccp);
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

                String _phoneNumber = phoneNumber.getText().toString();
                fullNumber = codePicker.getFullNumber() + _phoneNumber;
                final String _password = password.getText().toString();

                DocumentReference dr = fFirestore.collection("players").document(fullNumber);
                dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                        {
                             DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            if(_password.equals(document.get("password")))
                                 {
                                     Intent in1 = new Intent(login.this, main_Menu.class);
                                     startActivity(in1);
                                 }
                            else
                                {
                                     Toast.makeText(getApplicationContext(),"Incorrect Phone Number or Password!",Toast.LENGTH_LONG).show();
                                     password.setError("Incorrect Password");
                                     password.requestFocus();

                                }
                       }
                            else
                                {
                                    Log.d("TAG", "No such document");
                                    phoneNumber.setError("Incorrect Phone Number");
                                    phoneNumber.requestFocus();
                                }
                   }
                    }
               });

                break;

            case R.id.button2:
                Intent in2 = new Intent(login.this, SignUp.class);
                startActivity(in2);
                break;
        }
    }
}
