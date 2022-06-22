package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;


public class InboxChat extends AppCompatActivity {

    private ImageView threeDots;
    private LinearLayout layoutsDisplay;
    private ConstraintLayout reportAUserLayout;
    private ImageView reportUSers,close,doneBtn;
    private RadioButton r1,r2,r3,r4,r5,r6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_chat);


        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        r5 = findViewById(R.id.r5);
        r6 = findViewById(R.id.r6);



        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r2.setChecked(false);
                    r3.setChecked(false);
                    r4.setChecked(false);
                    r6.setChecked(false);
                    r5.setChecked(false);
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
                    r6.setChecked(false);
                    r5.setChecked(false);
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
                    r6.setChecked(false);
                    r5.setChecked(false);
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
                    r6.setChecked(false);
                    r5.setChecked(false);
                }


            }
        });


        r5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);
                    r2.setChecked(false);

                    r4.setChecked(false);
                    r4.setChecked(false);
                    r6.setChecked(false);
                }

            }
        });
        r6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);
                    r2.setChecked(false);
                    r3.setChecked(false);
                    r4.setChecked(false);
                    r5.setChecked(false);
                }


            }
        });



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InboxChat.super.onBackPressed();
            }
        });


        final boolean[] check = {false};

        threeDots = findViewById(R.id.threeDots);
        layoutsDisplay = findViewById(R.id.layoutsDisplay);
        reportUSers = findViewById(R.id.reportUSers);
        reportAUserLayout = findViewById(R.id.reportAUser);
        close = findViewById(R.id.close);
        doneBtn = findViewById(R.id.doneBtn);



        reportUSers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportAUserLayout.setVisibility(View.VISIBLE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportAUserLayout.setVisibility(View.GONE);
            }
        });
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportAUserLayout.setVisibility(View.GONE);
            }
        });




        threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check[0])
                {
                    layoutsDisplay.setVisibility(View.GONE);
                    check[0] =false;
                }
                else
                {
                    layoutsDisplay.setVisibility(View.VISIBLE);
                    check[0] = true;
                }

            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(InboxChat.this);
                builder1.setMessage("Are you sure to exit?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(InboxChat.this, SplashScreen.class));
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }
}