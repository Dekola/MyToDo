package com.kola.mytodo;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kola.mytodo.Fragment.IntroFragment;

import java.io.File;
import java.io.IOException;

public class AuthActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;

    String name, email, userId, imageUrl;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        checkForExistingUser();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.frame, new IntroFragment())
                .commit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                google_signIn();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//                // [START_EXCLUDE]
//                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//                // [START_EXCLUDE]
//                updateUI(null);
                // [END_EXCLUDE]
            }
        });

        askForPermissions();

    }

    private void google_signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 914);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 914) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(this, "signInResult:failed code=" + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            name = user.getDisplayName();
                            email = user.getEmail();
                            userId = user.getUid();

                            if (user.getPhotoUrl()!=null){
                                downloadImage(user.getPhotoUrl());
                            }else {
                                saveToDatabase();
                            }

                        } else {
                            Log.w("google", "signInWithCredential:failure", task.getException());
                            showToast("Authentication Failed.");
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            name = user.getDisplayName();
                            email = user.getEmail();
                            userId = user.getUid();

                            if (user.getPhotoUrl()!=null){
                                downloadImage(user.getPhotoUrl());
                            }else {
                                saveToDatabase();
                            }

                        } else {
                            Log.w("FacebookLogin", "signInWithCredential:failure", task.getException());
                            showToast("Authentication failed.");
                        }
                    }
                });
    }

    private void checkForExistingUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();
            userId = user.getUid();
            goToNextActivity();

//            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
//                @Override
//                public void onSuccess(GetTokenResult result) {
//                    String token = result.getToken();
//                }
//            });
//            boolean emailVerified = user.isEmailVerified();
        }}

    private void downloadImage(Uri photoUrl) {

            File imageFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "myToDo");
            if (!imageFolder.exists())imageFolder.mkdirs();

            File noMediaFile = new File(Environment.getExternalStorageDirectory() + File.separator + "myToDo/", ".nomedia");

            if (!noMediaFile.exists()){

                try {
                    noMediaFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        File imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + "myToDo/", email+".jpg");

        if (imageFile.exists()){

            saveToDatabase();

        }
        else {

            DownloadManager mgr = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(photoUrl);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("my ToDo")
                    .setDescription("Downloading Image")
                    .setDestinationInExternalPublicDir("/myToDo", email + ".jpg");

            mgr.enqueue(request);

            registerReceiver(onComplete, new IntentFilter(mgr.ACTION_DOWNLOAD_COMPLETE));
        }

        }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            saveToDatabase();
        }
    };
        private void saveToDatabase() {
            goToNextActivity();
        }

    private void goToNextActivity() {
        Intent it = new Intent(this, DrawerActivity.class);
//        it.putExtra("text",name+" "+email+" "+userId);
        it.putExtra("name",name);
        it.putExtra("email",email);
        startActivity(it);

        this.finish();
    }

    void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void askForPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheck != 0) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Storage access needed");
//                builder.setPositiveButton(android.R.string.ok, null);
//                builder.setMessage("please confirm Storage access");
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @TargetApi(Build.VERSION_CODES.M)
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions( new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    }
//                });
//                builder.show();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}