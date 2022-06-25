package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        SharedPreferences sharedPreferences = getSharedPreferences("My-Ref",MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");
        String profile = sharedPreferences.getString("profile","");


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userId!="")
                {
                    if(profile!="")
                    {
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(SplashScreen.this,CompleteProfile.class));
                        finish();
                    }
                }
                else
                {
                    startActivity(new Intent(SplashScreen.this,LoginSignup.class));
                    finish();
                }

            }
        },2500);
    }
}