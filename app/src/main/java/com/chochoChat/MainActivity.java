package com.chochoChat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chochoChat.Fragment.GroupChatFragment;
import com.chochoChat.Fragment.HomeFragment;
import com.chochoChat.Fragment.SettingFragment;
import com.chochoChat.Fragment.YourChat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    public static ConstraintLayout constraints;
    public static ImageView close,searchBtn;
    private ImageView chatIcons,homeIcon,profileFragment,groupIcon;

    public static RelativeLayout groupPopup;
    private EditText groupNames;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        close = findViewById(R.id.close);
        groupNames = findViewById(R.id.groupNames);
        groupPopup = findViewById(R.id.groupPopup);
        frameLayout = findViewById(R.id.framlayout);
        constraints = findViewById(R.id.constraints);
        groupIcon = findViewById(R.id.groupIcon);
        searchBtn = findViewById(R.id.searchBtn);

        homeIcon = findViewById(R.id.homeIcons);
        chatIcons = findViewById(R.id.chatIcons);
        profileFragment = findViewById(R.id.profileFragment);


        findViewById(R.id.closeImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupPopup.setVisibility(View.GONE);
            }
        });

        chatIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeIcon.setColorFilter(getResources().getColor(R.color.white));
                chatIcons.setColorFilter(getResources().getColor(R.color.purple_200));
                profileFragment.setColorFilter(getResources().getColor(R.color.white));
                groupIcon.setColorFilter(getResources().getColor(R.color.white));
                chatFragment();
            }
        });


        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeIcon.setColorFilter(getResources().getColor(R.color.purple_200));
                chatIcons.setColorFilter(getResources().getColor(R.color.white));
                profileFragment.setColorFilter(getResources().getColor(R.color.white));
                groupIcon.setColorFilter(getResources().getColor(R.color.white));
                homeFragment();
            }
        });
        profileFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeIcon.setColorFilter(getResources().getColor(R.color.white));
                chatIcons.setColorFilter(getResources().getColor(R.color.white));
                profileFragment.setColorFilter(getResources().getColor(R.color.purple_200));
                groupIcon.setColorFilter(getResources().getColor(R.color.white));
                profileFragments();
            }
        });
        groupIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeIcon.setColorFilter(getResources().getColor(R.color.white));
                chatIcons.setColorFilter(getResources().getColor(R.color.white));
                profileFragment.setColorFilter(getResources().getColor(R.color.white));
                groupIcon.setColorFilter(getResources().getColor(R.color.purple_200));
                groupFragment();
            }
        });

        homeFragment();

        SharedPreferences sharedPreferences = getSharedPreferences("My-Ref",MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");

        findViewById(R.id.doneBtns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String GroupName = groupNames.getText().toString();
                if(GroupName.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    String pushIds = String.valueOf(System.currentTimeMillis());
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("GroupName",GroupName);
                    hashMap.put("CreatedBy",userId);
                    hashMap.put("members",1);
                    databaseReference.child("Groups").child(pushIds).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            HashMap<String,Object> hashMap1 = new HashMap<>();
                            hashMap1.put(userId,true);
                            databaseReference.child("Groups").child(pushIds).child("GroupMembers").updateChildren(hashMap1);
                            Toast.makeText(MainActivity.this, "Group has been created", Toast.LENGTH_SHORT).show();
                            groupNames.setText("");
                            groupNames.setHint("Please Enter Group Name");
                            groupPopup.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });

    }
    public void groupFragment()
    {
        final Fragment fragment;
        fragment = new GroupChatFragment();
        loadFragment(fragment);
    }
    public void profileFragments()
    {
        final Fragment fragment;
        fragment = new SettingFragment();
        loadFragment(fragment);
    }
    public void chatFragment()
    {
        final Fragment fragment;
        fragment = new YourChat();
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