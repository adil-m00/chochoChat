package com.chochoChat.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chochoChat.InboxChat;
import com.chochoChat.Modal.UserModal;
import com.chochoChat.R;
import com.chochoChat.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    ArrayList<UserModal> userModals;
    MyAdapter myAdapter;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        userModals = new ArrayList<>();
        myAdapter=new MyAdapter(getActivity(),getActivity(),userModals);
        recyclerView.setAdapter(myAdapter);

        getUsers();

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    public void getUsers()
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModals.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    UserModal userModal = new UserModal();
                    userModal.setDate(dataSnapshot.child("Date").getValue().toString());
                    userModal.setAbout(dataSnapshot.child("About").getValue().toString());
                    userModal.setEmail(dataSnapshot.child("Email").getValue().toString());
                    userModal.setUserName(dataSnapshot.child("userName").getValue().toString());
                    userModal.setGender(dataSnapshot.child("Gender").getValue().toString());
                    userModal.setName(dataSnapshot.child("Name").getValue().toString());
                    userModal.setType(dataSnapshot.child("type").getValue().toString());
                    userModal.setImage(dataSnapshot.child("Image").getValue().toString());
                    userModal.setUserId(dataSnapshot.getKey());
                    userModals.add(userModal);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<UserModal> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {

            private ImageView userImage;
            private TextView Name;
            public MyViewHolder(View view) {
                super(view);

                userImage = view.findViewById(R.id.userImage);
                Name = view.findViewById(R.id.Name);
            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<UserModal> cartModelss){
            this.data =cartModelss;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_adapter, parent, false);
            return new MyAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder viewHolder, final int position) {
            UserModal userModal = data.get(position);

            try {
                Glide.with(getActivity()).load(userModal.getImage()).into(viewHolder.userImage);
            }
            catch (Exception ex)
            {

            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), InboxChat.class);
                    intent.putExtra("userId",userModal.getUserId());
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
//        return  5;
            return data.size();
        }

        public void setFilter(ArrayList<UserModal> newList){
            data=new ArrayList<>();
            data.addAll(newList);
            notifyDataSetChanged();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
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