package com.chochoChat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chochoChat.Fragment.ChatFragment;
import com.chochoChat.Fragment.HomeFragment;
import com.chochoChat.Fragment.ProfileFragment;
import com.randomchat.R;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    public static ConstraintLayout constraints;
    public static ImageView close,searchBtn;
    private ImageView chatIcons,homeIcon,profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        close = findViewById(R.id.close);

        frameLayout = findViewById(R.id.framlayout);
        constraints = findViewById(R.id.constraints);
        searchBtn = findViewById(R.id.searchBtn);

        homeIcon = findViewById(R.id.homeIcons);
        chatIcons = findViewById(R.id.chatIcons);
        profileFragment = findViewById(R.id.profileFragment);

        chatIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatIcons.setImageDrawable(getResources().getDrawable(R.drawable.chat_icon_sel));
                profileFragment.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon_unsel));
                chatFragment();
            }
        });


        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatIcons.setImageDrawable(getResources().getDrawable(R.drawable.chat_icon_unsel));
                profileFragment.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon_unsel));
                homeFragment();
            }
        });
        profileFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatIcons.setImageDrawable(getResources().getDrawable(R.drawable.chat_icon_unsel));
                profileFragment.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon_sel));
                profileFragments();
            }
        });

        homeFragment();
    }
    public void profileFragments()
    {
        final Fragment fragment;
        fragment = new ProfileFragment();
        loadFragment(fragment);
    }
    public void chatFragment()
    {
        final Fragment fragment;
        fragment = new ChatFragment();
        loadFragment(fragment);
    }

    public void homeFragment()
    {
        final Fragment fragment;
        fragment = new HomeFragment();
        loadFragment(fragment);
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framlayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Are you sure to exit?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
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



}