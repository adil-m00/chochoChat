package com.chochoChat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class CompleteProfile extends AppCompatActivity {

    private Spinner gender;
    private EditText names,abouts;
    private TextView dateofBirth;
    private ImageView profileImage;
    private ImageView selectImages;
    Uri imageUri;
    private String Date="";
    final Calendar myCalendar = Calendar.getInstance();
    private DatabaseReference databaseReference;

    StorageReference mStorageRef;
    private String ImageSelect=null;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        dateofBirth = findViewById(R.id.dateofBirth);
        profileImage = findViewById(R.id.profile);
        selectImages = findViewById(R.id.selectImages);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        gender = findViewById(R.id.genderSpinner);
        names = findViewById(R.id.names);
        abouts = findViewById(R.id.abouts);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CompleteProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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

        SharedPreferences sharedPreferences = getSharedPreferences("My-Ref",MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");

        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Names = names.getText().toString();
                String Abouts = abouts.getText().toString();
                if(Names.isEmpty() || Abouts.isEmpty() || Date == "")
                {
                    Toast.makeText(CompleteProfile.this, "all fields required", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Profile is updating");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    final StorageReference riversRef = mStorageRef.child("User/_"+System.currentTimeMillis());

                    riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("Name",Names);
                                    hashMap.put("About",Abouts);
                                    hashMap.put("Image",uri.toString());
                                    hashMap.put("Date",Date);
                                    hashMap.put("Gender",gender.getSelectedItem().toString());
                                    databaseReference.child(userId).updateChildren(hashMap);
                                    Toast.makeText(CompleteProfile.this, "Profile has been updated", Toast.LENGTH_SHORT).show();

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("profile",userId);
                                    editor.commit();
                                    editor.apply();
                                    startActivity(new Intent(CompleteProfile.this,MainActivity.class));
                                    finish();
                                }
                            });
                        }
                    });



                }
            }
        });



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
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date = (sdf.format(myCalendar.getTime()));
        dateofBirth.setText(sdf.format(myCalendar.getTime()));
    }

}