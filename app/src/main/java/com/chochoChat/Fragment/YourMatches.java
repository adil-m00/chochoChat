package com.chochoChat.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chochoChat.Adapter.AvatarModal;
import com.chochoChat.InboxChat;
import com.chochoChat.R;

import java.util.ArrayList;

public class YourMatches extends Fragment {


    public YourMatches() {
        // Required empty public constructor
    }


    private View view;
    private RecyclerView recyclerView;
    ArrayList<AvatarModal> avatarModals;

    MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_your_matches, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        avatarModals = new ArrayList<>();

        myAdapter=new MyAdapter(getActivity(),getActivity(),avatarModals);
        recyclerView.setAdapter(myAdapter);


        return view;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<AvatarModal> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {

            private ImageView chatIcon;
            public MyViewHolder(View view) {
                super(view);
                chatIcon = view.findViewById(R.id.chatIcon);

            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<AvatarModal> cartModelss){
            this.data =cartModelss;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.your_matches_adapter, parent, false);
            return new MyAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder viewHolder, final int position) {

            viewHolder.chatIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), InboxChat.class));
                }
            });




        }
        @Override
        public int getItemCount() {
//        return  5;
            return 3;
        }

        public void setFilter(ArrayList<AvatarModal> newList){
            data=new ArrayList<>();
            data.addAll(newList);
            notifyDataSetChanged();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}