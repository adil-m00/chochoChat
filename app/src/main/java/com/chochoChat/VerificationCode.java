package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.randomchat.R;


public class VerificationCode extends AppCompatActivity  implements View.OnClickListener,
        OnOtpCompletionListener {

    private ImageView validateButton;
    private OtpView otpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);






        initializeUi();
        setListeners();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificationCode.super.onBackPressed();
            }
        });
    }

    @Override public void onClick(View v) {
        if (v.getId() == R.id.signIn) {
            if(otpView.getText().length()==4)
            {
                startActivity(new Intent(VerificationCode.this,NewPassword.class));
                return;
            }
            else
            {
                Toast.makeText(this, "Enter Verification Code", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void initializeUi() {
        otpView = findViewById(R.id.otp_view);
        validateButton = findViewById(R.id.signIn);

    }

    private void setListeners() {
        validateButton.setOnClickListener(this);
        otpView.setOtpCompletionListener(this);
    }

    @Override public void onOtpCompleted(String otp) {
        // do Stuff

    }
}