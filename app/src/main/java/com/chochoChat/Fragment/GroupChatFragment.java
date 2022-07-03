package com.chochoChat.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chochoChat.MainActivity;
import com.chochoChat.MemberActivitySpecific;
import com.chochoChat.Modal.GroupChatModal;
import com.chochoChat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class GroupChatFragment extends Fragment {



    public GroupChatFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    ArrayList<GroupChatModal> groupChatModals;
    MyAdapter myAdapter;
    private DatabaseReference databaseReference;
    private String userId;
    private TextView searchHere;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        view.findViewById(R.id.addBtns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.groupPopup.setVisibility(View.VISIBLE);
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        searchHere = view.findViewById(R.id.searchHere);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        groupChatModals = new ArrayList<>();
        myAdapter=new MyAdapter(getActivity(),getActivity(),groupChatModals);
        recyclerView.setAdapter(myAdapter);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("My-Ref", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        getGroupChating(userId);


        searchHere.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String value = s.toString();
//                    System.out.println(results);
                ArrayList<GroupChatModal> newList=new ArrayList<>();
                for (GroupChatModal prodInfo :groupChatModals ){
                    String jobTitle=prodInfo.getGroupName().toLowerCase();

                    if (jobTitle.contains(value.toLowerCase())){
                        newList.add(prodInfo);
                    }
                }
                myAdapter.setFilter(newList);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void getGroupChating(String userIDS)
    {
        databaseReference.child("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatModals.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {

                    GroupChatModal modal = new GroupChatModal();
                    modal.setGroupId(dataSnapshot.getKey());
                    modal.setGroupAdminId(dataSnapshot.child("CreatedBy").getValue().toString());
                    modal.setGroupName(dataSnapshot.child("GroupName").getValue().toString());
                    modal.setMembers(dataSnapshot.child("members").getValue().toString());
                    if(dataSnapshot.child("GroupMembers").child(userIDS).exists() || dataSnapshot.child("CreatedBy").getValue().toString().equals(userId))
                    {
                        modal.setStatus("Yes");
                    }
                    else
                    {
                        modal.setStatus("No");
                    }
                    groupChatModals.add(modal);

                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<GroupChatModal> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {

            private TextView groupName,members;
            private Button joinNow;
            public MyViewHolder(View view) {
                super(view);
                groupName = view.findViewById(R.id.groupName);
                members = view.findViewById(R.id.members);
                joinNow = view.findViewById(R.id.joinNow);

            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<GroupChatModal> cartModelss){
            this.data =cartModelss;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_adapter, parent, false);
            return new MyAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder viewHolder, final int position) {
            GroupChatModal modal = data.get(position);

            viewHolder.groupName.setText(modal.getGroupName());
            viewHolder.members.setText(modal.getMembers()+" Members");

            if(modal.getStatus().equals("Yes"))
            {
                viewHolder.joinNow.setVisibility(View.GONE);
                if(modal.getGroupAdminId().equals(userId))
                {
                    viewHolder.joinNow.setVisibility(View.VISIBLE);
                    viewHolder.joinNow.setText("Remove Group?");

                    viewHolder.joinNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage("Are you sure you want to remove this group?.");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            databaseReference.child("Groups").child(modal.getGroupId()).removeValue();
                                            Toast.makeText(getActivity(), "Group has been removed", Toast.LENGTH_SHORT).show();
                                            myAdapter.notifyDataSetChanged();
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

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), MemberActivitySpecific.class).putExtra("groupId", modal.getGroupId()).putExtra("adminId", modal.getGroupAdminId()).putExtra("groupName", modal.getGroupName()));

                        }
                    });
                }
                else {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), MemberActivitySpecific.class).putExtra("groupId", modal.getGroupId()).putExtra("adminId", modal.getGroupAdminId()).putExtra("groupName", modal.getGroupName()));

                        }
                    });
                }
            }
            else if(modal.getStatus().equals("No"))
            {
                viewHolder.joinNow.setVisibility(View.VISIBLE);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Please Join Group", Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.joinNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("Groups").child(modal.getGroupId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                viewHolder.joinNow.setVisibility(View.GONE);
                                int members = Integer.parseInt(snapshot.child("members").getValue().toString());
                                members++;
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("members",members);
                                HashMap<String,Object> hashMap1 = new HashMap<>();
                                hashMap1.put(userId,true);
                                databaseReference.child("Groups").child(modal.getGroupId()).updateChildren(hashMap);
                                databaseReference.child("Groups").child(modal.getGroupId()).child("GroupMembers").updateChildren(hashMap1);
                                myAdapter.notifyDataSetChanged();
                                return;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

        }
        @Override
        public int getItemCount() {
//        return  5;
            return data.size();
        }

        public void setFilter(ArrayList<GroupChatModal> newList){
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