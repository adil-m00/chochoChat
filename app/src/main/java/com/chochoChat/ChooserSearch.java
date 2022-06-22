package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class ChooserSearch extends AppCompatActivity {


    private ImageView noBtn,okBtn,noBtn1,okBtn1,close1,close2;
    private ConstraintLayout constraints,constraints1;

    private RadioButton r1,r2,r3,r4;
    private RadioGroup grpsl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser_search);
        grpsl = findViewById(R.id.grps);

        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);



        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        r2.setChecked(false);
                        r3.setChecked(false);
                        r4.setChecked(false);
                    }

            }
        });
        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked)
               {
                   r1.setChecked(false);

                   r3.setChecked(false);
                   r4.setChecked(false);
               }


            }
        });
        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);
                    r2.setChecked(false);

                    r4.setChecked(false);
                }

            }
        });
        r4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);
                    r2.setChecked(false);
                    r3.setChecked(false);
                }


            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooserSearch.super.onBackPressed();
            }
        });
        noBtn = findViewById(R.id.noBtn);
        okBtn = findViewById(R.id.okBtn);
        noBtn1 = findViewById(R.id.noBtn1);
        okBtn1 = findViewById(R.id.okBtn2);
        constraints = findViewById(R.id.constraints);
        constraints1 = findViewById(R.id.constraints2);
        close1 = findViewById(R.id.close);
        close2 = findViewById(R.id.closes);


        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraints.setVisibility(View.GONE);
                ChooserSearch.super.onBackPressed();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraints.setVisibility(View.GONE);
                ChooserSearch.super.onBackPressed();

            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraints.setVisibility(View.GONE);
                constraints1.setVisibility(View.VISIBLE);
            }
        });


        okBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooserSearch.this,SearchResult.class));
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraints.setVisibility(View.VISIBLE);
                constraints1.setVisibility(View.GONE);
            }
        });
    }
}