package com.chochoChat.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chochoChat.EditProfile;
import com.chochoChat.R;

public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }
    private EditText userName,name,email,about;
    private TextView dob;
    private Spinner genderSelect;
    private ImageView saveChanges;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
}