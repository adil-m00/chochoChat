package com.chochoChat.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chochoChat.Adapter.AvatarModal;
import com.chochoChat.InboxChat;
import com.chochoChat.Modal.ChatFragmentModal;
import com.chochoChat.R;
import com.chochoChat.SplashScreen;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourChat extends Fragment {


    public YourChat() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView;
    ArrayList<ChatFragmentModal> chatModal;
    MyAdapter myAdapter;


    private DatabaseReference chatRef,userRef;


    private FirebaseAuth firebaseAuth;


    ArrayList<String> chatUsersId = new ArrayList<>();



    private ProgressDialog progressDialog;

    TimeAgoMessages messagesTimesAgo;


    private int messageSeen=0;

    ArrayList<Integer> messageSeenArray = new ArrayList<>();
    private EditText searchHere;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_your_chat, container, false);


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    firebaseAuth = FirebaseAuth.getInstance();

    userRef = FirebaseDatabase.getInstance().getReference("Users");

    chatRef = FirebaseDatabase.getInstance().getReference("Chat");




    searchHere = view.findViewById(R.id.searchHere);
    recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    chatModal = new ArrayList<>();
    myAdapter=new MyAdapter(getActivity(),getActivity(),chatModal);
        recyclerView.setAdapter(myAdapter);






        searchHere.setOnKeyListener(new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) &&
                    (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                return true;
            }
            return false;
        }
    });

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
}

    @Override
    public void onResume() {
        super.onResume();
        myAdapter.notifyDataSetChanged();
        getChatDetail();
    }

    public void getChatDetail()
    {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    progressDialog.dismiss();
                    chatModal.clear();
                    messageSeenArray.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(dataSnapshot.getKey().contains(firebaseAuth.getCurrentUser().getUid())){
                            ChatFragmentModal chatFragmentAdapter = null;
                            messageSeen=0;
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                                chatFragmentAdapter = dataSnapshot1.getValue(ChatFragmentModal.class);
                                String ids[]=dataSnapshot.getKey().split("_chat_");
                                if(ids[0].equals(firebaseAuth.getCurrentUser().getUid().toString()))
                                {
                                    chatFragmentAdapter.setSenderId(ids[0]);
                                    chatFragmentAdapter.setReceiverId(ids[1]);
                                    if(dataSnapshot1.child("senderId").getValue().equals(chatFragmentAdapter.getReceiverId()) && dataSnapshot1.child("status").getValue().equals("0"))
                                    {
                                        messageSeen +=1;
                                    }
                                }
                                else
                                {
                                    chatFragmentAdapter.setSenderId(ids[1]);
                                    chatFragmentAdapter.setReceiverId(ids[0]);
                                    if(dataSnapshot1.child("senderId").getValue().equals(chatFragmentAdapter.getReceiverId()) && dataSnapshot1.child("status").getValue().equals("0"))
                                    {
                                        messageSeen +=1;
                                    }
                                }

                            }


                            messageSeenArray.add(messageSeen);
                            chatModal.add(chatFragmentAdapter);
                            myAdapter.notifyDataSetChanged();

                        }
                    }
                    progressDialog.dismiss();
                    myAdapter.notifyDataSetChanged();
                }catch (Exception c){
                    progressDialog.dismiss();
                    c.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<ChatFragmentModal> data;
    Context context;
    Activity activity;
    String TAG;
    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private CircleImageView userProfile;
        private TextView userName,lastMessage,lastTime,messageSeen;
        public MyViewHolder(View view) {
            super(view);

            userName = view.findViewById(R.id.userName);
            userProfile = view.findViewById(R.id.userProfile);
            lastMessage = view.findViewById(R.id.lastMessage);
            lastTime = view.findViewById(R.id.lastTime);
            messageSeen = view.findViewById(R.id.messageLAsts);

        }
    }
    public MyAdapter(Context c, Activity a , ArrayList<ChatFragmentModal> cartModelss){
        this.data =cartModelss;
        context=c;
        activity=a;
        TAG="***Adapter";
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_chat_adapter, parent, false);
        return new MyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.MyViewHolder viewHolder, final int position) {

        Locale LocaleBylanguageTag = Locale.forLanguageTag("en");
        messagesTimesAgo = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();



        ChatFragmentModal chatFragmentAdapter = data.get(position);




        if(messageSeenArray.get(position)==0)
        {
            viewHolder.messageSeen.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.messageSeen.setVisibility(View.VISIBLE);
            viewHolder.messageSeen.setText(String.valueOf(messageSeenArray.get(position)));
        }




        viewHolder.lastTime.setText(TimeAgo.from(chatFragmentAdapter.getTimestamp(),messagesTimesAgo));

        if(chatFragmentAdapter.getMessageType().equals("Image"))
        {
            viewHolder.lastMessage.setText("Image");
        }
        else
        {
            viewHolder.lastMessage.setText(chatFragmentAdapter.getMessage());
        }

        userRef.child(chatFragmentAdapter.getReceiverId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Image").exists())
                {
                    Glide.with(getActivity()).load(snapshot.child("Image").getValue().toString()).into(viewHolder.userProfile);
                }
                viewHolder.userName.setText(snapshot.child("Name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Are you sure to Delete this Chat?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(chatFragmentAdapter.getSenderId().equals(firebaseAuth.getCurrentUser().getUid()))
                                {
                                    chatRef.child(chatFragmentAdapter.getSenderId()+"_chat_"+chatFragmentAdapter.getReceiverId()).removeValue();
                                }
                                else
                                {
                                    chatRef.child(chatFragmentAdapter.getReceiverId()+"_chat_"+chatFragmentAdapter.getSenderId()).removeValue();
                                }

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
                //2. now setup to change color of the button
                alert11.setOnShowListener(new DialogInterface.OnShowListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple_700));
                        alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purple_700));
                    }
                });
                alert11.show();

                return false;
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InboxChat.class);
                intent.putExtra("userId",chatFragmentAdapter.getReceiverId());
                startActivity(intent);

            }
        });




    }
    @Override
    public int getItemCount() {
//        return  5;
        return data.size();
    }

    public void setFilter(ArrayList<ChatFragmentModal> newList){
        data=new ArrayList<>();
        data.addAll(newList);
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}



//    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//        ArrayList<AvatarModal> data;
//        Context context;
//        Activity activity;
//        String TAG;
//        public class MyViewHolder extends RecyclerView.ViewHolder  {
//
//            public MyViewHolder(View view) {
//                super(view);
//
//
//            }
//        }
//        public MyAdapter(Context c, Activity a , ArrayList<AvatarModal> cartModelss){
//            this.data =cartModelss;
//            context=c;
//            activity=a;
//            TAG="***Adapter";
//        }
//        @Override
//        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.your_chat_adapter, parent, false);
//            return new MyAdapter.MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final MyAdapter.MyViewHolder viewHolder, final int position) {
//
//
//
//
//        }
//        @Override
//        public int getItemCount() {
////        return  5;
//            return 3;
//        }
//
//        public void setFilter(ArrayList<AvatarModal> newList){
//            data=new ArrayList<>();
//            data.addAll(newList);
//            notifyDataSetChanged();
//        }
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//    }
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