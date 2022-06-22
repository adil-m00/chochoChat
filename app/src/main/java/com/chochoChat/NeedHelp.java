package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;


public class NeedHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NeedHelp.super.onBackPressed();
            }
        });

    }
}