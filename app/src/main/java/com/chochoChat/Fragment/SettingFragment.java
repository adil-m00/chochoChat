package com.chochoChat.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chochoChat.CompleteProfile;
import com.chochoChat.EditProfile;
import com.chochoChat.MainActivity;
import com.chochoChat.R;
import com.chochoChat.Settings;
import com.chochoChat.SplashScreen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }
    private EditText userName,name,email,about;
    private TextView dob;
    private Spinner genderSelect;
    private ImageView saveChanges,selectImages;
    private CircleImageView profile;
    private DatabaseReference databaseReference;
    private String Date="";
    final Calendar myCalendar = Calendar.getInstance();
    Uri imageUri;
    private String imageSelect="";
    private ProgressDialog progressDialog;
    StorageReference mStorageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        progressDialog = new ProgressDialog(getActivity());

        view.findViewById(R.id.settingIcons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Settings.class));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        selectImages = view.findViewById(R.id.selectImages);
        profile = view.findViewById(R.id.profile);
        userName = view.findViewById(R.id.userName);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        about = view.findViewById(R.id.about);
        dob = view.findViewById(R.id.dobs);
        genderSelect = view.findViewById(R.id.genders);
        saveChanges = view.findViewById(R.id.saveBtn);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("My-Ref",MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");
        getData(userId);

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

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String UserName = userName.getText().toString();
                String About = about.getText().toString();
                if(Name.isEmpty() || UserName.isEmpty() || About.isEmpty() || Date == "")
                {
                    Toast.makeText(getActivity(), "all fields required", Toast.LENGTH_SHORT).show();
                }
                else if(imageSelect=="")
                {
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("Name",Name);
                    hashMap.put("userName",UserName);
                    hashMap.put("About",About);
                    hashMap.put("Date",Date);
                    hashMap.put("Gender",genderSelect.getSelectedItem().toString());
                    databaseReference.child(userId).updateChildren(hashMap);
                    Toast.makeText(getActivity(), "Profile has been updated", Toast.LENGTH_SHORT).show();

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
                                    hashMap.put("Name",Name);
                                    hashMap.put("userName",UserName);
                                    hashMap.put("About",About);
                                    hashMap.put("Image",uri.toString());
                                    hashMap.put("Date",Date);
                                    hashMap.put("Gender",genderSelect.getSelectedItem().toString());
                                    databaseReference.child(userId).updateChildren(hashMap);
                                    Toast.makeText(getActivity(), "Profile has been updated", Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                    });


                }
            }
        });
        view.findViewById(R.id.logouts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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
            imageSelect="1234";
            profile.setImageURI(imageUri);

        }
    }
    public void getData(String userId)
    {
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    email.setText(snapshot.child("Email").getValue().toString());
                    userName.setText(snapshot.child("userName").getValue().toString());
                    name.setText(snapshot.child("Name").getValue().toString());
                    dob.setText(snapshot.child("Date").getValue().toString());
                    about.setText(snapshot.child("About").getValue().toString());
                    Date = ""+snapshot.child("Date").getValue().toString();
                    Glide.with(getActivity()).load(snapshot.child("Image").getValue().toString()).into(profile);
                    if(snapshot.child("Gender").getValue().equals("Male"))
                    {
                        genderSelect.setSelection(0);
                    }
                    else
                    {
                        genderSelect.setSelection(1);
                    }
                }
                catch (Exception ex)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date = (sdf.format(myCalendar.getTime()));
        dob.setText(sdf.format(myCalendar.getTime()));
    }
    public void logout()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("My-Ref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId",null);
                        editor.putString("profile",null);

                        editor.commit();
                        editor.apply();
                        startActivity(new Intent(getActivity(), SplashScreen.class));
                        getActivity().finish();
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