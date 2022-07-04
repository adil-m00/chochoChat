package com.chochoChat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chochoChat.Modal.MyMessageModelImage;
import com.chochoChat.Modal.MyMessageModelText;
import com.chochoChat.Modal.OtherMessageModelImage;
import com.chochoChat.Modal.OtherMessageModelText;
import com.chochoChat.Modal.UserInfoModal;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberActivitySpecific extends AppCompatActivity {
    private SlimAdapter slimAdapter;
    private List<Object> messagesList = new ArrayList<>();



    private RecyclerView recyclerViewMessages;
    //    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages;

    private long time;


    private FirebaseAuth firebaseAuth;
    private  String currentUserId;
    private DatabaseReference chatRef;
    private DatabaseReference userRef,demandsChecks,reportRef,adminRef;
    LinearLayoutManager linearLayoutManager;


    public static UserInfoModal otherUserModal=new UserInfoModal();
    private String otherUserId;
    private ProgressDialog progressDialog;

    private String combination;
    private EditText chatMessage;
    private ImageView sendMessage;
    private TextView homeIcon;
    private  String type="";
    private String postId;




//    recording


    private File recordFile;
    StorageReference mStorageRef;
    private ImageView attachImage;
    private RelativeLayout typeMessageLayout;
    private LinearLayout linearLayout;


    File mAudioFile;


    public static ImageView popupImage,closeImage;
    public static RelativeLayout popupRelative;


    public  ImageView doneBtnsReport,closeImageReport;
    public RelativeLayout reportLayout;
    public EditText reportText,sendMessagesEdit;

//    call

    private String groupId,groupAdmin,groupName;
    private TextView joinNow;
    private TextView username;
    private ImageView chatText;

    private ArrayList<String> fcmGets = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_specific);
        groupName = getIntent().getStringExtra("groupName");
        groupId = getIntent().getStringExtra("groupId");
        groupAdmin = getIntent().getStringExtra("adminId");

        sendMessagesEdit  = findViewById(R.id.sendMessagesEdit);




        if(getIntent().getStringExtra("type")!=null)
        {
            type = getIntent().getStringExtra("type");
        }
        postId = getIntent().getStringExtra("postId");


        SharedPreferences sharedPreferences = getSharedPreferences("My-Ref", Context.MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("userId","");

        otherUserId = getIntent().getStringExtra("otherUserId");


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        chatRef = FirebaseDatabase.getInstance().getReference("Groups");
        reportRef = FirebaseDatabase.getInstance().getReference("GroupsReport");
        demandsChecks = FirebaseDatabase.getInstance().getReference("Notification");
        adminRef = FirebaseDatabase.getInstance().getReference("Admin");
        mStorageRef = FirebaseStorage.getInstance().getReference();



        linearLayoutManager = new LinearLayoutManager(MemberActivitySpecific.this);

        messages = new ArrayList<>();


        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        homeIcon = findViewById(R.id.username);

        popupImage = findViewById(R.id.popupImage);
        closeImage = findViewById(R.id.closeImage);
        popupRelative = findViewById(R.id.popupRelative);
        reportLayout = findViewById(R.id.reportLayout);
        doneBtnsReport = findViewById(R.id.doneBtnsReport);
        reportText = findViewById(R.id.reportText);
        closeImageReport = findViewById(R.id.closeImageReport);

        username = findViewById(R.id.username);
        chatText = findViewById(R.id.chatText);
        joinNow = findViewById(R.id.joinNow);
        username.setText(groupName);

        if(groupAdmin.equals(currentUserId))
        {
            chatText.setVisibility(View.VISIBLE);
            joinNow.setVisibility(View.GONE);
        }
        else
        {
            chatText.setVisibility(View.INVISIBLE);
            joinNow.setVisibility(View.VISIBLE);
            joinNow.setText("Leave Now");
        }

//
//        containerView.findViewById(R.id.backs).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });


        //messageAdapter = new MessageAdapter(messages,ChatInbox.this,ChatInbox.this);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        recyclerViewMessages.setHasFixedSize(false);



        //recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());








        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupRelative.setVisibility(View.GONE);
            }
        });

        slimAdapter = SlimAdapter.create()
                .register(R.layout.my_image_model, new SlimInjector<MyMessageModelImage>() {
                    @Override
                    public void onInject(MyMessageModelImage data, IViewInjector injector) {
                        //injector.text(R.id.otherTimesImage, data.getTimestamp());\
                        ImageView imageSender = (ImageView) injector.findViewById(R.id.imageSender);
                        Glide.with(MemberActivitySpecific.this).load(data.getMessage()).into(imageSender);
                        injector.clicked(R.id.imageSender, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupRelative.setVisibility(View.VISIBLE);
                                try {
                                    Glide.with(MemberActivitySpecific.this)
                                            .load(data.getMessage()).into(popupImage);
                                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }
                })
                .register(R.layout.my_text_model, new SlimInjector<MyMessageModelText>() {
                    @Override
                    public void onInject(MyMessageModelText data, IViewInjector injector) {
                        injector.text(R.id.senderText, data.getMessage());
                        TextView sendderTexts = (TextView) injector.findViewById(R.id.senderText);
                        sendderTexts.setText(data.getMessage());
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        String dateString = formatter.format(new Date(Long.parseLong(data.getTimestamp())));
                        Linkify.addLinks(sendderTexts, Linkify.WEB_URLS);
//                        TextView times = (TextView) injector.findViewById(R.id.timess);

                        sendderTexts.setLinkTextColor(ContextCompat.getColor(MemberActivitySpecific.this,
                                R.color.linkColor));
                    }
                })
                .register(R.layout.other_user_image_model, new SlimInjector<OtherMessageModelImage>() {
                    @Override
                    public void onInject(OtherMessageModelImage data, IViewInjector injector) {
                        //injector.text(R.id.otherTimesImage, data.getTimestamp());\
                        ImageView otherImage = (ImageView) injector.findViewById(R.id.otherImage);
                        TextView names = (TextView) injector.findViewById(R.id.names);
                        names.setText(data.getUserName());
//                        TextView times = (TextView) injector.findViewById(R.id.timess);
                        Glide.with(MemberActivitySpecific.this).load(data.getMessage()).into(otherImage);

                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        String dateString = formatter.format(new Date(Long.parseLong(data.getTimestamp())));
//                        times.setText(dateString);

                        injector.clicked(R.id.otherImage, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupRelative.setVisibility(View.VISIBLE);
                                try {
                                    Glide.with(MemberActivitySpecific.this)
                                            .load(data.getMessage()).into(popupImage);
                                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        CircleImageView profileUser = (CircleImageView)
                                injector.findViewById(R.id.profileUser);

                        profileUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                userRef.child(data.getSenderId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.child("block").exists())
                                        {
                                            Toast.makeText(MemberActivitySpecific.this, "Admin has blocked this user", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else if(snapshot.child("Block").exists())
                                        {
                                            if(!snapshot.child("Block").child(currentUserId).getValue().equals(true)) {
                                                Toast.makeText(MemberActivitySpecific.this, "This user has been blocked you", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            else
                                            {
                                                Toast.makeText(MemberActivitySpecific.this, "you blocked this user", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                        else
                                        {
//                                            startActivity(new Intent(MemberActivitySpecific.this,UserProfileView.class).putExtra("userId",data.getSenderId()));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                        try {
                            Glide.with(MemberActivitySpecific.this).load(data.getUserProfileImage()).into(profileUser);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }


                    }
                })
                .register(R.layout.other_user_text_model, new SlimInjector<OtherMessageModelText>() {
                    @Override
                    public void onInject(OtherMessageModelText data, IViewInjector injector) {
//                        TextView timess = (TextView) injector.findViewById(R.id.timess);

                        TextView sendderTexts = (TextView) injector.findViewById(R.id.otherText);
                        TextView names = (TextView) injector.findViewById(R.id.names);
                        names.setText(data.getUserName());
                        sendderTexts.setText(data.getMessage());
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        String dateString = formatter.format(new Date(Long.parseLong(data.getTimestamp())));
                        Linkify.addLinks(sendderTexts, Linkify.WEB_URLS);
                        sendderTexts.setLinkTextColor(ContextCompat.getColor(MemberActivitySpecific.this,
                                R.color.linkColor));
                        CircleImageView profileUser = (CircleImageView)
                                injector.findViewById(R.id.profileUser);
                        profileUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                userRef.child(data.getSenderId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.child("block").exists())
                                        {
                                            Toast.makeText(MemberActivitySpecific.this, "Admin has blocked this user", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else if(snapshot.child("Block").child(currentUserId).exists())
                                        {
                                            if(!snapshot.child("Block").child(currentUserId).getValue().equals(true)) {
                                                Toast.makeText(MemberActivitySpecific.this, "This user has been blocked you", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            else
                                            {
                                                Toast.makeText(MemberActivitySpecific.this, "You blocked this user", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                        else
                                        {
//                                            startActivity(new Intent(MemberActivitySpecific.this,UserProfileView.class).putExtra("userId",data.getSenderId()));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                        try {
                            Glide.with(MemberActivitySpecific.this).load(data.getUserProfileImage()).into(profileUser);

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }

                    }
                })
                .attachTo(recyclerViewMessages);
        slimAdapter.updateData(messagesList);







        getMessages(groupId,groupAdmin);
        closeImageReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportLayout.setVisibility(View.GONE);
            }
        });

        setListener();
    }

    private void setListener() {



        findViewById(R.id.images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

        doneBtnsReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Messages = reportText.getText().toString();
                if(Messages.isEmpty())
                {
                    Toast.makeText(MemberActivitySpecific.this, "Please Write message here", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("groupId",groupId);
                    hashMap.put("groupAdmin",groupAdmin);
                    hashMap.put("groupName",groupName);
                    hashMap.put("Message",Messages);
                    hashMap.put("currentUserId",currentUserId);
                    reportRef.child(String.valueOf(System.currentTimeMillis())).updateChildren(hashMap);
                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot dataSnapshot:snapshot.getChildren())
                            {
                                String FCM = dataSnapshot.child("FCM").getValue().toString();
                                sendNotification("Android",FCM,"Report","User has sent you group report");
                            }
                            reportLayout.setVisibility(View.GONE);
                            Toast.makeText(MemberActivitySpecific.this, "Report has been sent to admin thank you", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        findViewById(R.id.reports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberActivitySpecific.this);
                builder1.setMessage("Are you sure you want to report / flag this user?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                reportLayout.setVisibility(View.VISIBLE);
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
                        alert11.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.purple_700));
                        alert11.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.purple_700));
                    }
                });
                alert11.show();
            }
        });

    }




    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void debug(String log) {
        Log.d("VarunJohn", log);
    }








    public void getMessages(String GroupId,String groupAdmins)
    {
        chatRef.child(GroupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    joinNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int members = Integer.parseInt(snapshot.child("members").getValue().toString());
                            members--;
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("members",members);

                            chatRef.child(GroupId).updateChildren(hashMap);
                            chatRef.child(GroupId).child("GroupMembers").child(currentUserId).removeValue();
                            onBackPressed();
                            finish();
                        }
                    });
                    for(DataSnapshot dataSnapshot:snapshot.child("GroupMembers").getChildren())
                    {
                        fcmGets.clear();
                        userRef.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                fcmGets.add(snapshot.child("FCM").getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });



        chatRef.child(GroupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesList.clear();
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot : snapshot.child("Chat").getChildren()) {
                    String message = dataSnapshot.child("message").getValue().toString();
                    String messageType = dataSnapshot.child("messageType").getValue().toString();
                    String senderId = dataSnapshot.child("senderId").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    String userProfile = dataSnapshot.child("userImage").getValue().toString();
                    String userName = dataSnapshot.child("userName").getValue().toString();
                    String timeStamp = dataSnapshot.child("timestamp").getValue().toString();


                    if (currentUserId
                            .equalsIgnoreCase(senderId)) {
                        if (messageType.equalsIgnoreCase("Image")) {
                            MyMessageModelImage model = new MyMessageModelImage();
                            model.setMessage(message);
                            model.setSenderId(senderId);
                            model.setUserName(userName);
                            model.setStatus(status);
                            model.setTimestamp(timeStamp);
                            messagesList.add(model);
                        }
                       else {
                            MyMessageModelText model = new MyMessageModelText();
                            model.setMessage(message);
                            model.setUserName(userName);
                            model.setSenderId(senderId);
                            model.setStatus(status);
                            model.setTimestamp(timeStamp);
                            messagesList.add(model);
                        }
                    } else {
                        if (messageType.equalsIgnoreCase("Image")) {
                            OtherMessageModelImage model = new OtherMessageModelImage();
                            model.setMessage(message);
                            model.setUserName(userName);
                            model.setSenderId(senderId);
                            model.setStatus(status);
                            model.setUserProfileImage(userProfile);
                            model.setTimestamp(timeStamp);
                            messagesList.add(model);
                        } else {
                            OtherMessageModelText model = new OtherMessageModelText();
                            model.setMessage(message);
                            model.setSenderId(senderId);
                            model.setUserName(userName);
                            model.setStatus(status);
                            model.setUserProfileImage(userProfile);
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


        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Message = sendMessagesEdit.getText().toString();
                if(Message.isEmpty())
                {
                    sendMessagesEdit.setError("Please Enter Message");
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
                            hashMap.put("userImage",snapshot.child("Image").getValue().toString());
                            hashMap.put("senderId",currentUserId);
                            hashMap.put("userName",snapshot.child("Name").getValue().toString());
                            hashMap.put("status","0");
                            hashMap.put("timestamp",System.currentTimeMillis());



                            for(int i=0;i<fcmGets.size();i++)
                            {
                                sendNotification("Android",fcmGets.get(i),snapshot.child("Name").getValue().toString(),"Sent you a message");
                            }

                            chatRef.child(GroupId).child("Chat").child(String.valueOf(System.currentTimeMillis())).setValue(hashMap);

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






    public static void sendNotification(final String osType,final String fcmToken, final String title, final String body) {
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
                            .header("Authorization", "key=AAAAKMk4qlo:APA91bGD_OwwfPYTmr7vGLKtZgHeAIViKGlLd5a4pQRCEAUZl--g4sZacOZoR418Jtu1kOTFv84UZq16I9bis18Mo3J_VM6dW-G8RiT3Ya7y7US_eOi-Y7fSxGGsfcSiNd1tFPSuiGiA") //Legacy Server Key
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




    public static void sendFirstDemnds(final String osType,final String fcmToken, final String title, final String body,final String others) {
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
                    dataJson.put("Chat",others);
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
                            .header("Authorization", "key=AAAA-wAkOA8:APA91bHPhX1at_w7TOYTL2uyCXpRufr5xFnyHHtnhUPvO1KKTNp0uT4F9xDuv7UIhy96cXm1Lxd_dfRUlT0Fos_vFPairHNA_BlG4EPU3LI9ZpXZQWWJyMNyt58SbPBzsBS7bLDNRA80") //Legacy Server Key
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==100 && resultCode==RESULT_OK){
            Uri imageUri = data.getData();


            AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberActivitySpecific.this);
            builder1.setMessage("Are you sure you want to send this image?.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            progressDialog.setMessage("Please Wait");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();


                            final StorageReference riversRef = mStorageRef.child("ChatImages/_"+System.currentTimeMillis());


                            riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri imageProfile) {

                                            userRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    HashMap<String,Object> hashMap = new HashMap<>();
                                                    hashMap.put("message",imageProfile.toString());
                                                    hashMap.put("messageType","Image");
                                                    hashMap.put("userName",snapshot.child("Name").getValue().toString());
                                                    hashMap.put("userImage",snapshot.child("image").getValue().toString());
                                                    hashMap.put("receiverId",otherUserId);
                                                    hashMap.put("senderId",currentUserId);
                                                    hashMap.put("status","0");
                                                    hashMap.put("timestamp",System.currentTimeMillis());



                                                    for(int i=0;i<fcmGets.size();i++)
                                                    {
                                                        sendNotification("Android",fcmGets.get(i),snapshot.child("Name").getValue().toString(),"Sent you a message");
                                                    }

                                                    chatRef.child(groupId).child("Chat").child(String.valueOf(System.currentTimeMillis())).setValue(hashMap);
                                                    progressDialog.dismiss();

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });



                                        }
                                    });
                                }
                            });
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
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    public void keyboardOff()
    {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}