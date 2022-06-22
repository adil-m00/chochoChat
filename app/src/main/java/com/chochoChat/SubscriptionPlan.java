package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;


public class SubscriptionPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscriptionPlan.super.onBackPressed();
            }
        });





    }
}