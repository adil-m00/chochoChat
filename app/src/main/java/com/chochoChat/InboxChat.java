package com.chochoChat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chochoChat.Modal.InboxModal;
import com.chochoChat.Modal.MyMessageModelImage;
import com.chochoChat.Modal.MyMessageModelText;
import com.chochoChat.Modal.OtherMessageModelImage;
import com.chochoChat.Modal.OtherMessageModelText;
import com.chochoChat.Modal.UserInfoModal;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
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

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class InboxChat extends AppCompatActivity {

    private ImageView threeDots;
    private LinearLayout layoutsDisplay;
    private ConstraintLayout reportAUserLayout;
    private ImageView reportUSers,close,doneBtn;
    private RadioButton r1,r2,r3,r4,r5,r6;


    private RecyclerView recyclerView;
    ArrayList<InboxModal> inboxModals;
    LinearLayoutManager linearLayoutManager;
    private List<Object> messagesList = new ArrayList<>();


    //    MyAdapter myAdapter;
    private DatabaseReference chatRef,userRef;
    private String combination;
    private EditText chatMessage;
    private ImageView sendMessage;
    private String otherUserId;
    private ProgressDialog progressDialog;

    UserInfoModal otherUserModal=new UserInfoModal();
    private ImageView attachFile;
    StorageReference mStorageRef;

    private Uri imageUri;


    private String ImageSelect="";
    private ImageView imageSent;
    private FirebaseAuth firebaseAuth;
    private  String currentUserId;

    private CircleImageView userImage;
    private TextView userName;
    TimeAgoMessages messagesTimesAgo;
    private TextView status;
    private SlimAdapter slimAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_chat);


        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        r5 = findViewById(R.id.r5);
        r6 = findViewById(R.id.r6);



        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r2.setChecked(false);
                    r3.setChecked(false);
                    r4.setChecked(false);
                    r6.setChecked(false);
                    r5.setChecked(false);
                }

            }
        });
        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);

                    r3.setChecked(false);
                    r4.setChecked(false);
                    r6.setChecked(false);
                    r5.setChecked(false);
                }


            }
        });
        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);
                    r2.setChecked(false);

                    r4.setChecked(false);
                    r6.setChecked(false);
                    r5.setChecked(false);
                }

            }
        });
        r4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);
                    r2.setChecked(false);
                    r3.setChecked(false);
                    r6.setChecked(false);
                    r5.setChecked(false);
                }


            }
        });


        r5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);
                    r2.setChecked(false);

                    r4.setChecked(false);
                    r4.setChecked(false);
                    r6.setChecked(false);
                }

            }
        });
        r6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    r1.setChecked(false);
                    r2.setChecked(false);
                    r3.setChecked(false);
                    r4.setChecked(false);
                    r5.setChecked(false);
                }


            }
        });



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InboxChat.super.onBackPressed();
            }
        });


        final boolean[] check = {false};

        threeDots = findViewById(R.id.threeDots);
        layoutsDisplay = findViewById(R.id.layoutsDisplay);
        reportUSers = findViewById(R.id.reportUSers);
        reportAUserLayout = findViewById(R.id.reportAUser);
        close = findViewById(R.id.close);
        doneBtn = findViewById(R.id.doneBtn);



        reportUSers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportAUserLayout.setVisibility(View.VISIBLE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportAUserLayout.setVisibility(View.GONE);
            }
        });
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportAUserLayout.setVisibility(View.GONE);
            }
        });




        threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check[0])
                {
                    layoutsDisplay.setVisibility(View.GONE);
                    check[0] =false;
                }
                else
                {
                    layoutsDisplay.setVisibility(View.VISIBLE);
                    check[0] = true;
                }

            }
        });



        chatMessage = findViewById(R.id.sendMessagesEdit);
        sendMessage = findViewById(R.id.send);
        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.homeIcon);
        status = findViewById(R.id.status);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        inboxModals = new ArrayList<>();
        recyclerView.setItemAnimator(new DefaultItemAnimator());


//        myAdapter=new MyAdapter(ChatInbox.this,ChatInbox.this,inboxModals);
//        recyclerView.setAdapter(myAdapter);

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        chatRef = FirebaseDatabase.getInstance().getReference("Chat");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        currentUserId = firebaseAuth.getCurrentUser().getUid().toString();
        otherUserId = getIntent().getStringExtra("userId");




        slimAdapter = SlimAdapter.create()
                .register(R.layout.my_image_model, new SlimInjector<MyMessageModelImage>() {
                    @Override
                    public void onInject(MyMessageModelImage data, IViewInjector injector) {
                        //injector.text(R.id.otherTimesImage, data.getTimestamp());\
                        ImageView imageSender = (ImageView) injector.findViewById(R.id.imageSender);
                        Glide.with(InboxChat.this).load(data.getMessage()).into(imageSender);
                        injector.clicked(R.id.imageSender, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                try {
//                                    Glide.with(ChatInbox.this)
//                                            .load(data.getMessage()).into(ChatInbox.popupImage);
//                                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                                }
//                                catch (Exception ex)
//                                {
//                                    ex.printStackTrace();
//                                }
                            }
                        });
                    }
                })
                .register(R.layout.my_text_model, new SlimInjector<MyMessageModelText>() {
                    @Override
                    public void onInject(MyMessageModelText data, IViewInjector injector) {
                        injector.text(R.id.senderText, data.getMessage());
//                        TextView times = (TextView) injector.findViewById(R.id.timess);
//                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//                        String dateString = formatter.format(new Date(Long.parseLong(data.getTimestamp())));
//                        times.setText(dateString);
                    }
                })
                .register(R.layout.other_user_image_model, new SlimInjector<OtherMessageModelImage>() {
                    @Override
                    public void onInject(OtherMessageModelImage data, IViewInjector injector) {
                        //injector.text(R.id.otherTimesImage, data.getTimestamp());\
                        ImageView otherImage = (ImageView) injector.findViewById(R.id.otherImage);
//                        TextView times = (TextView) injector.findViewById(R.id.timess);
                        Glide.with(InboxChat.this).load(data.getMessage()).into(otherImage);

//                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//                        String dateString = formatter.format(new Date(Long.parseLong(data.getTimestamp())));
//                        times.setText(dateString);

                        injector.clicked(R.id.otherImage, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                try {
//                                    Glide.with(ChatInbox.this)
//                                            .load(data.getMessage()).into(ChatInbox.popupImage);
//                                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                                }
//                                catch (Exception ex)
//                                {
//                                    ex.printStackTrace();
//                                }
                            }
                        });
                        {
                            CircleImageView profileUser = (CircleImageView)
                                    injector.findViewById(R.id.profileUser);
                            try {
                                Glide.with(InboxChat.this).load(otherUserModal.getImage()).into(profileUser);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }

                        }
                    }
                })
                .register(R.layout.other_user_text_model, new SlimInjector<OtherMessageModelText>() {
                    @Override
                    public void onInject(OtherMessageModelText data, IViewInjector injector) {
                        injector.text(R.id.otherText, data.getMessage());
//                        TextView timess = (TextView) injector.findViewById(R.id.timess);

//                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//                        String dateString = formatter.format(new Date(Long.parseLong(data.getTimestamp())));
//                        timess.setText(dateString);
                        if(otherUserModal.getImage()!=null)
                        {
                            CircleImageView profileUser = (CircleImageView)
                                    injector.findViewById(R.id.profileUser);
                            try {
                                Glide.with(InboxChat.this).load(otherUserModal.getImage()).into(profileUser);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }

                        }
                    }
                })
                .attachTo(recyclerView);
        slimAdapter.updateData(messagesList);


        getMessages(otherUserId);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        MessageCheckCombinations(otherUserId);

        findViewById(R.id.images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }



    public void MessageCheckCombinations(String otherUser)
    {
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                combination="";
                inboxModals.clear();
                messagesList.clear();
                if(snapshot.child(otherUser+"_chat_"+firebaseAuth.getCurrentUser().getUid()).exists()){
                    combination=otherUser+"_chat_"+firebaseAuth.getCurrentUser().getUid();
                    seenMessags(combination);
                }else if(snapshot.child(firebaseAuth.getCurrentUser().getUid()+"_chat_"+otherUser).exists()){
                    combination=firebaseAuth.getCurrentUser().getUid()+"_chat_"+otherUser;
                    seenMessags(combination);
                }else {
                    combination=firebaseAuth.getCurrentUser().getUid()+"_chat_"+otherUser;
                }

                chatRef.child(combination).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        inboxModals.clear();
                        messagesList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            String message = dataSnapshot.child("message").getValue().toString();
                            String messageType = dataSnapshot.child("messageType").getValue().toString();
                            String receiverId = dataSnapshot.child("receiverId").getValue().toString();
                            String senderId = dataSnapshot.child("senderId").getValue().toString();
                            String status = dataSnapshot.child("status").getValue().toString();
                            String timeStamp = dataSnapshot.child("timestamp").getValue().toString();


                            InboxModal inboxChatModal = new InboxModal();

                            inboxChatModal.setMessage(message);
                            inboxChatModal.setType(messageType);
                            inboxChatModal.setReceiverId(receiverId);
                            inboxChatModal.setSenderId(senderId);
                            inboxChatModal.setStatus(status);
                            inboxChatModal.setTimestamp(timeStamp);


                            inboxModals.add(inboxChatModal);


                            if(currentUserId
                                    .equalsIgnoreCase(senderId)) {
                                if(messageType.equalsIgnoreCase("Image")) {
                                    MyMessageModelImage model = new MyMessageModelImage();
                                    model.setMessage(message);
                                    model.setReceiverId(receiverId);
                                    model.setSenderId(senderId);
                                    model.setStatus(status);
                                    model.setTimestamp(timeStamp);
                                    messagesList.add(model);
                                }
                                else {
                                    MyMessageModelText model = new MyMessageModelText();
                                    model.setMessage(message);
                                    model.setReceiverId(receiverId);
                                    model.setSenderId(senderId);
                                    model.setStatus(status);
                                    model.setTimestamp(timeStamp);
                                    messagesList.add(model);
                                }
                            }
                            else {
                                if(messageType.equalsIgnoreCase("Image")) {
                                    OtherMessageModelImage model = new OtherMessageModelImage();
                                    model.setMessage(message);
                                    model.setReceiverId(receiverId);
                                    model.setSenderId(senderId);
                                    model.setStatus(status);
                                    model.setTimestamp(timeStamp);
                                    messagesList.add(model);
                                }
                                else {
                                    OtherMessageModelText model = new OtherMessageModelText();
                                    model.setMessage(message);
                                    model.setReceiverId(receiverId);
                                    model.setSenderId(senderId);
                                    model.setStatus(status);
                                    model.setTimestamp(timeStamp);
                                    messagesList.add(model);
                                }
                            }


                        }
                        Collections.reverse(messagesList);
                        slimAdapter.notifyDataSetChanged();
//                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void seenMessags(String Combinations)
    {
        chatRef.child(Combinations).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(dataSnapshot.child("senderId").getValue().equals(otherUserId) && !(dataSnapshot.child("senderId").getValue().equals(firebaseAuth.getCurrentUser().getUid().toString())))
                    {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("status","1");
                        chatRef.child(combination).child(dataSnapshot.getKey()).updateChildren(hashMap);

                        Log.d("Combinations",""+dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getMessages(String otherUser)
    {

        userRef.child(otherUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshots",""+snapshot);
                progressDialog.dismiss();
                userName.setText(snapshot.child("Name").getValue().toString());

                if(snapshot.child("status").getValue().toString().equals("Online"))
                {
                    status.setText("Online");
                }
                else {
                    Locale LocaleBylanguageTag = Locale.forLanguageTag("en");
                    messagesTimesAgo = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                    status.setText(TimeAgo.from(Long.parseLong(snapshot.child("status").getValue().toString()),messagesTimesAgo));

                }



                otherUserModal = snapshot.getValue(UserInfoModal.class);
                try {
                    Glide.with(InboxChat.this).load(snapshot.child("Image").getValue().toString()).into(userImage);
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });








        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                combination="";
                inboxModals.clear();
                messagesList.clear();
                if(snapshot.child(otherUser+"_chat_"+firebaseAuth.getCurrentUser().getUid()).exists()){
                    combination=otherUser+"_chat_"+firebaseAuth.getCurrentUser().getUid();

                }else if(snapshot.child(firebaseAuth.getCurrentUser().getUid()+"_chat_"+otherUser).exists()){
                    combination=firebaseAuth.getCurrentUser().getUid()+"_chat_"+otherUser;
                }else {
                    combination=firebaseAuth.getCurrentUser().getUid()+"_chat_"+otherUser;
                }

                chatRef.child(combination).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        inboxModals.clear();
                        messagesList.clear();
                        progressDialog.dismiss();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            String message = dataSnapshot.child("message").getValue().toString();
                            String messageType = dataSnapshot.child("messageType").getValue().toString();
                            String receiverId = dataSnapshot.child("receiverId").getValue().toString();
                            String senderId = dataSnapshot.child("senderId").getValue().toString();
                            String status = dataSnapshot.child("status").getValue().toString();
                            String timeStamp = dataSnapshot.child("timestamp").getValue().toString();


                            InboxModal inboxChatModal = new InboxModal();

                            inboxChatModal.setMessage(message);
                            inboxChatModal.setType(messageType);
                            inboxChatModal.setReceiverId(receiverId);
                            inboxChatModal.setSenderId(senderId);
                            inboxChatModal.setStatus(status);
                            inboxChatModal.setTimestamp(timeStamp);


                            inboxModals.add(inboxChatModal);



                            if(currentUserId
                                    .equalsIgnoreCase(senderId)) {
                                if(messageType.equalsIgnoreCase("Image")) {
                                    MyMessageModelImage model = new MyMessageModelImage();
                                    model.setMessage(message);
                                    model.setReceiverId(receiverId);
                                    model.setSenderId(senderId);
                                    model.setStatus(status);
                                    model.setTimestamp(timeStamp);
                                    messagesList.add(model);
                                }
                                else {
                                    MyMessageModelText model = new MyMessageModelText();
                                    model.setMessage(message);
                                    model.setReceiverId(receiverId);
                                    model.setSenderId(senderId);
                                    model.setStatus(status);
                                    model.setTimestamp(timeStamp);
                                    messagesList.add(model);
                                }
                            }
                            else {
                                if(messageType.equalsIgnoreCase("Image")) {
                                    OtherMessageModelImage model = new OtherMessageModelImage();
                                    model.setMessage(message);
                                    model.setReceiverId(receiverId);
                                    model.setSenderId(senderId);
                                    model.setStatus(status);
                                    model.setTimestamp(timeStamp);
                                    messagesList.add(model);
                                }
                                else {
                                    OtherMessageModelText model = new OtherMessageModelText();
                                    model.setMessage(message);
                                    model.setReceiverId(receiverId);
                                    model.setSenderId(senderId);
                                    model.setStatus(status);
                                    model.setTimestamp(timeStamp);
                                    messagesList.add(model);
                                }
                            }

                        }
                        Collections.reverse(messagesList);
//                        myAdapter.notifyDataSetChanged();
                        slimAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Message = chatMessage.getText().toString();
                if(Message.isEmpty())
                {
                    chatMessage.setError("Please Enter Message");
                    return;
                }
                else
                {

                    userRef.child(firebaseAuth.getCurrentUser().getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("message",Message);
                            hashMap.put("messageType","Text");
                            hashMap.put("receiverId",otherUserId);
                            hashMap.put("senderId",currentUserId);
                            hashMap.put("status","0");
                            hashMap.put("timestamp",System.currentTimeMillis());




                            sendNotification("Android",otherUserModal.getFCM(),snapshot.child("Name").getValue().toString(),"Sent you a message",currentUserId);
                            chatRef.child(combination).child(String.valueOf(System.currentTimeMillis())).setValue(hashMap);

                            chatMessage.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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






            progressDialog.setTitle("...");
            progressDialog.setTitle("Please Wait Image is sending");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            final StorageReference riversRef = mStorageRef.child("ChatImage/_"+System.currentTimeMillis());
            riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();




                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("message",uri.toString());
                            hashMap.put("messageType","Image");
                            hashMap.put("receiverId",otherUserId);
                            hashMap.put("senderId",currentUserId);
                            hashMap.put("status","0");
                            hashMap.put("timestamp",System.currentTimeMillis());

                            chatRef.child(combination).child(String.valueOf(System.currentTimeMillis())).setValue(hashMap);

                            chatMessage.setText("");

                            slimAdapter.notifyDataSetChanged();
//                            myAdapter.notifyDataSetChanged();



                        }
                    });
                }
            });
            return;

        }
    }




//    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//        ArrayList<InboxModal> data;
//        Context context;
//        Activity activity;
//        String TAG;
//        public class MyViewHolder extends RecyclerView.ViewHolder  {
//
//            private ImageView chatIcon;
//            private ImageView receiverImage;
//            private TextView sender,receiver;
//
//            private CircleImageView profileUser;
//            private TextView otherText,senderText;
//            private ImageView imageSender,otherImage;
//
//            public MyViewHolder(View view) {
//                super(view);
//
//
//
//                senderText = view.findViewById(R.id.senderText);
//                profileUser = view.findViewById(R.id.profileUser);
//                otherText = view.findViewById(R.id.otherText);
//
//                imageSender = view.findViewById(R.id.imageSender);
//                otherImage = view.findViewById(R.id.otherImage);
//
//            }
//        }
//        public MyAdapter(Context c, Activity a , ArrayList<InboxModal> cartModelss){
//            this.data =cartModelss;
//            context=c;
//            activity=a;
//            TAG="***Adapter";
//        }
//        @Override
//        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.chat_inbox_adapter, parent, false);
//            return new MyAdapter.MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final MyAdapter.MyViewHolder viewHolder, final int position) {
//
//            InboxModal inboxChatAdapter = data.get(position);
//
//            if(inboxChatAdapter.getType().equals("Image")) {
//
//                if (inboxChatAdapter.getSenderId().equals(currentUserId)) {
//
//                    viewHolder.imageSender.setVisibility(View.VISIBLE);
//                    viewHolder.otherImage.setVisibility(View.GONE);
//                    viewHolder.profileUser.setVisibility(View.GONE);
//                    viewHolder.senderText.setVisibility(View.GONE);
//
//
//
//                    try {
//                        Glide.with(ChatInbox.this).load(inboxChatAdapter.getMessage()).into(viewHolder.imageSender);
//                    }
//                    catch (Exception ex)
//                    {
//
//                    }
//
//
//
//                } else {
//
//
//                    viewHolder.imageSender.setVisibility(View.GONE);
//                    viewHolder.otherImage.setVisibility(View.VISIBLE);
//                    viewHolder.imageSender.setVisibility(View.GONE);
//                    viewHolder.otherText.setVisibility(View.GONE);
//
//                    try {
//                        Glide.with(ChatInbox.this).load(inboxChatAdapter.getMessage()).into(viewHolder.otherImage);
//                    }
//                    catch (Exception ex)
//                    {
//
//                    }
//
//
//                    if(otherUserModal.getImage()!=null)
//                    {
//                        viewHolder.profileUser.setVisibility(View.VISIBLE);
//                        try {
//                            Glide.with(ChatInbox.this).load(otherUserModal.getImage()).into(viewHolder.profileUser);
//                        }
//                        catch (Exception ex)
//                        {
//
//                        }
//
//                    }
//
//
//
//                }
//            }
//            else
//            {
//                if (inboxChatAdapter.getSenderId().equals(currentUserId)) {
//
//                    viewHolder.senderText.setVisibility(View.VISIBLE);
//                    viewHolder.otherText.setVisibility(View.GONE);
//                    viewHolder.profileUser.setVisibility(View.GONE);
//                    viewHolder.imageSender.setVisibility(View.GONE);
//                    viewHolder.otherImage.setVisibility(View.GONE);
//                    viewHolder.senderText.setText(inboxChatAdapter.getMessage());
//                } else {
//                    if(otherUserModal.getImage()!=null)
//                    {
//                        viewHolder.profileUser.setVisibility(View.VISIBLE);
//                        try {
//                            Glide.with(ChatInbox.this).load(otherUserModal.getImage()).into(viewHolder.profileUser);
//                        }
//                        catch (Exception ex)
//                        {
//
//                        }
//
//                    }
//                    viewHolder.otherText.setVisibility(View.VISIBLE);
//                    viewHolder.senderText.setVisibility(View.GONE);
//                    viewHolder.imageSender.setVisibility(View.GONE);
//                    viewHolder.otherImage.setVisibility(View.GONE);
//
//
//                    viewHolder.otherText.setText(inboxChatAdapter.getMessage());
//                }
//            }
//
//
//        }
//        @Override
//        public int getItemCount() {
////        return  5;
//            return data.size();
//        }
//
//        public void setFilter(ArrayList<InboxModal> newList){
//            data=new ArrayList<>();
//            data.addAll(newList);
//            notifyDataSetChanged();
//        }
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//    }
//


    public static void sendNotification(final String osType,final String fcmToken, final String title, final String body,String others) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MediaType JSON
                            = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", body);
                    dataJson.put("title", title);
                    dataJson.put("f",others);
                    json.put("data", dataJson);
                    json.put("to", fcmToken);
                    if(osType.equals("Apple"))
                    {
                        json.put("notification", dataJson);
                    }
                    Log.e("finalResponse","token is "+fcmToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
//                        .header("Authorization", "key=AIzaSyANhp-yl7w4fKmgD-cuV_7U72CKCb3UA78") //Legacy Server Key
                            .header("Authorization", "key=AAAAMVVEZKE:APA91bFAN49EtlWpzwy0BZ-Us7Y5CmRdkbrduIFij2Log4cMZgj8HLwJMCIksqsH91LrgW_Y2o54l3bPyARDPmSmOyu5Im6Q8rtyQF2CG3UhnXMoWdzmlqEvS0IcA2pvZZjZIKscdMC8") //Legacy Server Key
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.e("finalResponse", finalResponse);
                } catch (Exception e) {
                    //Log.d(TAG,e+"");
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

}