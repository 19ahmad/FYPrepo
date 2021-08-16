package com.example.myapplication.externalService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Number_verification extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    String _verificationCode;
    TextView phoneNumber;
    Button verifyBtn;
    EditText verificationCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);
        this.setTitle("Number Verification");

        verifyBtn = findViewById(R.id.verify_button);
        progressBar = findViewById(R.id.progress_bar);
        phoneNumber = findViewById(R.id.phone_number);
        verificationCode = findViewById(R.id.verify_code);
        progressBar.setVisibility(View.GONE);

        String _phoneNo = getIntent().getStringExtra("phoneNumber");
        phoneNumber.setText(_phoneNo);
        sendVerificationCode(_phoneNo);

        //click verifyBtn for verification of phone number

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verificationCode.getText().toString();
                if(code.isEmpty() || code.length()<6){
                    verificationCode.setError("Wrong Verification Code");
                    verificationCode.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);

            }
        });
    }

    private void sendVerificationCode(String phoneNo)
    {
        String completePhoneNumber = "+92" + phoneNo;
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(completePhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallbacks)
                .setActivity(this)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+92" + phoneNo,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                (Activity) TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            _verificationCode = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Number_verification.this,e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };
    private void verifyCode(String manualCode){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(_verificationCode, manualCode);
        signInTheUser(credential);
    }

    private void signInTheUser(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(Number_verification.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent1 = new Intent(getApplicationContext(), Import_chat.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);

                }
                else{
                    Toast.makeText(Number_verification.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}