package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.randomchat.R;

public class NewPassword extends AppCompatActivity {

    private EditText password,newPassword;
    private ImageView passwordEye,newPasswordEye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);


        password = findViewById(R.id.editText2);
        newPassword = findViewById(R.id.confirmPassword);
        passwordEye = findViewById(R.id.passwordEye);
        newPasswordEye = findViewById(R.id.confimEye);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPassword.super.onBackPressed();
            }
        });




        passwordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    passwordEye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    //Show Password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEye.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    //Hide Password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        newPasswordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    newPasswordEye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    //Show Password
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    newPasswordEye.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    //Hide Password
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}