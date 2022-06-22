package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class SignUpScreen extends AppCompatActivity {



    private EditText passwordTexts,confirmText;
    private ImageView passwordEye,confirEye;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText email,password;
    private ImageView login;
    private ProgressDialog progressDialog;
    String fcmToken;


    GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    private final int RC_SIGN_IN  =123;
    private FirebaseAuth mAuth;



    //    facebook
    private LoginButton loginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);


        passwordTexts = findViewById(R.id.passwordText);
        confirmText = findViewById(R.id.editText4);

        passwordEye = findViewById(R.id.eyePassword);
        confirEye = findViewById(R.id.eyeConfirm);



        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpScreen.this,CompleteProfile.class));
            }
        });

        findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpScreen.this,TermsAndConditions.class));
            }
        });
        findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpScreen.this,PrivacyPolicy.class));
            }
        });





        confirEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    confirEye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    //Show Password
                    confirmText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    confirEye.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    //Hide Password
                    confirmText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });





        passwordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordTexts.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    passwordEye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    //Show Password
                    passwordTexts.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEye.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    //Hide Password
                    passwordTexts.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }


}