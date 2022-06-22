package com.chochoChat.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chochoChat.ChooserSearch;
import com.chochoChat.MainActivity;
import com.chochoChat.SplashScreen;
import com.chochoChat.R;
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private ImageView searchQuestions,responded;
    private View view;

    private ImageView filters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        filters = view.findViewById(R.id.filters);
        responded = view.findViewById(R.id.responded);



        view.findViewById(R.id.searchQuestions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChooserSearch.class));
            }
        });

        searchQuestions = view.findViewById(R.id.searchQuestions);

        filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.constraints.setVisibility(View.VISIBLE);
            }
        });

        MainActivity.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.constraints.setVisibility(View.GONE);
            }
        });

        MainActivity.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChooserSearch.class));
            }
        });



        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Are you sure to exit?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
        });
        return view;
    }


}