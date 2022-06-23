package com.chochoChat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;


public class LoginScreen extends AppCompatActivity {


    private ImageView eyeBtn;
    private EditText password,email;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ImageView login;
    private ProgressDialog progressDialog;
    String fcmToken;


    GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    private final int RC_SIGN_IN  =123;
    private FirebaseAuth mAuth;



    //    facebook
    private LoginButton loginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);



        findViewById(R.id.forgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this,ForgotPassword.class));
            }
        });

        findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this,TermsAndConditions.class));
            }
        });
        findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this,PrivacyPolicy.class));
            }
        });


        password = findViewById(R.id.passwordText);
        email = findViewById(R.id.editText2);



        eyeBtn = findViewById(R.id.eyeBtn);

        eyeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    eyeBtn.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    //Show Password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    eyeBtn.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                    //Hide Password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        login = findViewById(R.id.signIn);


        fcmToken="";

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                if(Email.isEmpty() || Password.isEmpty())
                {
                    Toast.makeText(LoginScreen.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setTitle("Please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String userId = firebaseAuth.getCurrentUser().getUid().toString();
                                databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists())
                                        {
                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("FCM",fcmToken);

                                            databaseReference.child(userId).updateChildren(hashMap);
                                            if(dataSnapshot.child("userType").getValue().equals("User"))
                                            {
                                                SharedPreferences sharedPreferences = getSharedPreferences("My-Ref",MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("userId",userId);
                                                editor.putString("userType","user");
                                                editor.commit();
                                                editor.apply();
                                                startActivity(new Intent(LoginScreen.this,MainActivity.class));
                                                finish();
                                            }
                                            else
                                            {
                                                SharedPreferences sharedPreferences = getSharedPreferences("My-Ref",MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("userId",userId);
                                                editor.putString("userType","company");
                                                editor.commit();
                                                editor.apply();
                                                startActivity(new Intent(LoginScreen.this,MainActivity.class));
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginScreen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });







        //        google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginScreen.this, gso);





        findViewById(R.id.imageView12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //        facebook

        mCallbackManager = CallbackManager.Factory.create();


        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login Results",""+loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());


            }
            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
            }
        });

        findViewById(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);

        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

            mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String photoUrl = account.getPhotoUrl().toString();
                        // Variable holding the original String portion of the url that will be replaced
                        String originalPieceOfUrl = "s96-c";

                        // Variable holding the new String portion of the url that does the replacing, to improve image quality
                        String newPieceOfUrlToAdd = "s400-c";

                        if(photoUrl!="")
                        {
                            String newString = photoUrl.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
                            updateUI(mAuth.getCurrentUser().getEmail(),mAuth.getCurrentUser().getDisplayName(),newString);
                        }
                        else
                        {
                            updateUI(mAuth.getCurrentUser().getEmail(),mAuth.getCurrentUser().getDisplayName(),photoUrl);
                        }







                    }
                    else
                    {
                        Toast.makeText(LoginScreen.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });





        } catch (ApiException e) {
            Toast.makeText(LoginScreen.this, "Sign in Failed", Toast.LENGTH_SHORT).show();
        }




    }


    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d("Tokens",token.getUserId());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //user = mFirebaseAuth.getCurrentUser();

                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    try {


                                        // Application code
                                        Log.i("Response", "onCompleted: response: " + response.toString());

                                        String email = mAuth.getCurrentUser().getEmail();
                                        String profile = mAuth.getCurrentUser().getPhotoUrl().toString();
                                        String id = mAuth.getCurrentUser().getUid();
                                        String Name = mAuth.getCurrentUser().getDisplayName();

                                        String ids=object.getString("id");
                                        JSONObject pictureObj = object.getJSONObject("picture");
                                        JSONObject pictureData = pictureObj.getJSONObject("data");
                                        final String ImageURL = pictureData.getString("url");
                                        String imagePath = "http://graph.facebook.com/" + ids + "/picture?type=large";
                                        Log.e("response_Image", "resposne image " + ImageURL);
                                        imagePath = imagePath.replace("http:", "https:");


                                        Log.i("Response", "onCompleted: response: " + profile);
                                        updateUI(email,Name,profile+"?type=large");
                                    }
                                    catch (Exception e)
                                    {

                                    }


                                }
                            });

                            Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                parameters.putString("fields", "id,name,email,picture");
                            parameters.putString("fields", "id,name,email,picture,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();

                        } else {
                            Toast.makeText(LoginScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void updateUI(String email,String Name,String profileUrl)
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId = mAuth.getCurrentUser().getUid().toString();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("FCM",fcmToken);
                hashMap.put("Email",fcmToken);
                hashMap.put("type","social");
                hashMap.put("Name",Name);

                databaseReference.child(userId).updateChildren(hashMap);

                SharedPreferences sharedPreferences = getSharedPreferences("My-Ref",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId",userId);
                editor.putString("userType","user");
                editor.commit();
                editor.apply();
                startActivity(new Intent(LoginScreen.this,MainActivity.class));
                finish();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}