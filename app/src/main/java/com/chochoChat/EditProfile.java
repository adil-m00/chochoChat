package com.chochoChat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;


import java.util.ArrayList;

public class EditProfile extends AppCompatActivity {



    private Spinner gender,country,city,education,smoke,alcohol,lookingFor;

    private ArrayList<String> genderLabel = new ArrayList<>();
    private ArrayList<String> countryLabel = new ArrayList<>();
    private ArrayList<String> CityLabel = new ArrayList<>();
    private ArrayList<String> educationLabel = new ArrayList<>();
    private ArrayList<String> smokeLabel = new ArrayList<>();
    private ArrayList<String> alcoholLabel = new ArrayList<>();
    private ArrayList<String> lookingForLabel = new ArrayList<>();


    private ImageView profileImage;
    private ImageView selectImages;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile.super.onBackPressed();
            }
        });

        profileImage = findViewById(R.id.profile);
        selectImages = findViewById(R.id.selectImages);


        gender = findViewById(R.id.genderSpinner);
        country = findViewById(R.id.countrySpinner);
        city = findViewById(R.id.citySpinner);
        education = findViewById(R.id.educationSpinner);
        smoke = findViewById(R.id.smokeSpinner);
        alcohol = findViewById(R.id.alcoholSpinner);
        lookingFor = findViewById(R.id.lookingFor);




        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                selectImages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 100);
                    }
                });
            }
        });






        bindingSpinner();



        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this,NeedHelp.class));
            }
        });


    }


    public void bindingSpinner()
    {
        genderLabel.add("Gender");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_text, R.id.tv_promo_txt, genderLabel);
        // Drop down layout style
        dataAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);


        countryLabel.add("Country");
        ArrayAdapter<String> dataAdapters = new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_text, R.id.tv_promo_txt, countryLabel);
        // Drop down layout style
        dataAdapters.setDropDownViewResource(R.layout.spinner_drop_down);
        // attaching data adapter to spinner
        country.setAdapter(dataAdapters);

//        city

        CityLabel.add("City");
        ArrayAdapter<String> dataAdapterC = new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_text, R.id.tv_promo_txt, CityLabel);
        // Drop down layout style
        dataAdapterC.setDropDownViewResource(R.layout.spinner_drop_down);
        // attaching data adapter to spinner
        city.setAdapter(dataAdapterC);



//Education
        educationLabel.add("Education");
        ArrayAdapter<String> dataAdapterE= new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_text, R.id.tv_promo_txt, educationLabel);
        // Drop down layout style
        dataAdapterE.setDropDownViewResource(R.layout.spinner_drop_down);
        // attaching data adapter to spinner
        education.setAdapter(dataAdapterE);



//        smoke


        smokeLabel.add("Smoke");
        ArrayAdapter<String> dataAdapterS= new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_text, R.id.tv_promo_txt, smokeLabel);
        // Drop down layout style
        dataAdapterS.setDropDownViewResource(R.layout.spinner_drop_down);
        // attaching data adapter to spinner
        smoke.setAdapter(dataAdapterS);



//        alcohol label
        alcoholLabel.add("Alcohol");
        ArrayAdapter<String> dataAdapterA= new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_text, R.id.tv_promo_txt, alcoholLabel);
        // Drop down layout style
        dataAdapterA.setDropDownViewResource(R.layout.spinner_drop_down);
        // attaching data adapter to spinner
        alcohol.setAdapter(dataAdapterA);



//        looking for
        lookingForLabel.add("Looking for");
        ArrayAdapter<String> dataAdapterL= new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_text, R.id.tv_promo_txt, lookingForLabel);
        // Drop down layout style
        dataAdapterL.setDropDownViewResource(R.layout.spinner_drop_down);
        // attaching data adapter to spinner
        lookingFor.setAdapter(dataAdapterL);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && resultCode==RESULT_OK){
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);

        }
    }


}
