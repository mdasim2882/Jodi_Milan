package com.jodimilans.matrimonial.ActivityConatiner.SignUp;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.jodimilans.matrimonial.ActivityConatiner.Body.HomeActivity;
import com.jodimilans.matrimonial.ActivityConatiner.Interfaces.LoadRegActivity;
import com.jodimilans.matrimonial.ActivityConatiner.SignUp.RegisterUser.RegisterActivity;
import com.jodimilans.matrimonial.HelperClasses.PrefVariables;
import com.jodimilans.matrimonial.R;
import com.jodimilans.matrimonial.ViewPagerAdapter.mViewPagerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity implements LoadRegActivity {
    private static final int RC_SIGN_IN = 9001;
    public static final String CHANNEL_ID = "NotifyOreo";

    FirebaseAuth mAuth;
    ViewPager viewPager;
    Button mLoginButton, mEmailLoginButton, mRegisterBtn, mGuestLoginBtn;
    String TAG = getClass().getSimpleName();
    private GoogleSignInClient mGoogleSignInClient;
    LoadRegActivity loadRegActivity;


    SharedPreferences sharedPreferences;

    RelativeLayout r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(task -> {
                    String msg = "Enabled";
                    if (!task.isSuccessful()) {
                        msg = "Disabled";
                    }
                    Log.d("SETTER SUB: ", msg);
                    //Toast.makeText(th, msg, Toast.LENGTH_SHORT).show();
                });

        createNotificationChannel();


        try {
            //Shared preferences is used to save login session after authentication on the same device
            sharedPreferences = getSharedPreferences(PrefVariables.LOGIN_STATS, Context.MODE_PRIVATE);
            Boolean statusLogin = sharedPreferences.getBoolean(PrefVariables.ISLOGIN, false);
            if (statusLogin) {
                //
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
        }

        loadRegActivity=this;
        mLoginButton = findViewById(R.id.google_sign_in_btn);
        mEmailLoginButton = findViewById(R.id.email_sign_in_btn);
        mRegisterBtn = findViewById(R.id.emai_register_btn);
        mGuestLoginBtn = findViewById(R.id.guest_sign_in_btn);
        // Add a view pager for better UI/UX
        setMyViewPager();
        mAuth = FirebaseAuth.getInstance();
        r = findViewById(R.id.basell);
        // Configure Google Sign In
        configureGoogleSignIn();


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "NotifyOreo";
//            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
//            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_new_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed" + e.getMessage());
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        String a = "Email: " + user.getEmail()
                                + "\nName: " + user.getDisplayName() + "\nL= " + user.getProviderData();
                        Log.d(TAG, "onComplete: USER DATA:\n" + a);

                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                        if (acct != null) {
                            String personName = acct.getDisplayName();
                            String personGivenName = acct.getGivenName();
                            String personFamilyName = acct.getFamilyName();
                            String personEmail = acct.getEmail();
                            String personId = acct.getId();
                            Uri personPhoto = acct.getPhotoUrl();
                            Log.d(TAG, "onComplete: RESULT--> " + "Name: " + personName + "\n" +
                                    "Email: " + personEmail + "\n" +
                                    "ID: " + personId + "\n" +
                                    "Photo: " + personPhoto + "\n" +
                                    "PersonGiven Name: " + personGivenName + "\n");
                            if (mAuth.getCurrentUser() != null) {
                                FirebaseFirestore.getInstance()
                                        .collection("Users")
                                        .get()
                                        .addOnCompleteListener(queryDocumentSnapshots -> {
                                            Log.d(TAG, "firebaseAuthWithGoogle() called with: idToken = [" + queryDocumentSnapshots.getResult().getDocuments()   + "]");
                                            if (queryDocumentSnapshots.getResult().getDocuments().contains(mAuth.getCurrentUser().getUid())) {
                                                loadRegActivity.loadregAct();
                                                Log.d(TAG, "firebaseAuthWithGoogle not having any such document");
                                            }
                                            else {
                                                Intent intent = new Intent(this, RegisterActivity.class);
                                                intent.putExtra("fromGoogleLogin", true);
                                                startActivity(intent);
                                            }
                                        });
                            }
                        }


//                            updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(r, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                    }

                    // ...
                });
    }


    private void setMyViewPager() {
        viewPager = findViewById(R.id.startview_pager);
        viewPager.setBackgroundColor(Color.TRANSPARENT);
        viewPager.setAdapter(new mViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setBackgroundColor(Color.TRANSPARENT);
        tabLayout.setupWithViewPager(viewPager, true);


    }

    public void setCurrentItem(int item, boolean smoothscroller) {
        viewPager.setCurrentItem(item, smoothscroller);
    }

    public void signIn(View view) {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void loginUserasGuest(View view) {
//        Log.d(TAG, "logout: Done");
//        mAuth.signOut();
//
//        mGoogleSignInClient.signOut().addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                     //   updateUI(null);
//                    }
//                });
        Boolean statusLogin = sharedPreferences.getBoolean(PrefVariables.ISLOGIN, false);
        if(!statusLogin)
            startActivity(new Intent(this, HomeActivity.class));
    }

    public void loginPress(View view) {
        gotoActivity(UserLoginActivity.class);
    }

    private void popupSelector(ArrayAdapter<String> shownAdapter) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.jodi_milan_logo);
        builderSingle.setTitle("Select One Name:-");


        final ArrayAdapter<String> arrayAdapter = shownAdapter;

        builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            Toast.makeText(this, "Item selected: " + strName, Toast.LENGTH_SHORT).show();

        });
        builderSingle.show();
    }

    private void gotoActivity(Class<?> to) {
        Intent i = new Intent(LoginActivity.this, to);
        startActivity(i);
    }

    public void registerMe(View view) {
        gotoActivity(RegisterActivity.class);
    }

    @Override
    public void loadregAct() {
        Intent i=new Intent(LoginActivity.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

    }
}