package com.chochoChat.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chochoChat.R;
import com.google.android.material.tabs.TabLayout;
import com.chochoChat.Adapter.ViewPagerAdapter;
import com.chochoChat.SplashScreen;


public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }


    private View view;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        tabLayout=view.findViewById(R.id.tabLayout);
        viewPager=view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("YOUR MATCHES"));
        tabLayout.addTab(tabLayout.newTab().setText("YOUR CHATS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(),getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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