package com.example.jodimilan.ActivityConatiner.SignUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.HelperClasses.PrefVariables;
import com.example.jodimilan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.UUID;

public class UserLoginActivity extends AppCompatActivity {
    public static final String JODI_MILAN = "Jodi Milan";
    public static final String ADMIN_EMAIL = "jodimilanmatrimoni@gmail.com";
    private final String TAG = getClass().getSimpleName();
    public static final int GALLERY_CODE = 1001;
    EditText logEmail, logPass;
    private FirebaseAuth fAuth;

    StorageReference storageReference;
    private EditText forgotEmail;
    private TextView resLinkSet;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        setUpToolbar();
        storageReference = FirebaseStorage.getInstance().getReference();

        fAuth = FirebaseAuth.getInstance();
        logEmail = findViewById(R.id.email_login_edt);
        logPass = findViewById(R.id.login_pass_edt);

        Boolean requestAdmin = getIntent().getBooleanExtra("requestAdmin", false);


    }

    private void createAccount(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "createUserWithEmail:success");
                        FirebaseUser user = fAuth.getCurrentUser();
                        try {
                            setAdminName(user);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Log.e(TAG, "Create Account: \n " +
                                "UID : " + fAuth.getUid() +
                                "Current User UID : " + user.getUid() +
                                "\n EMAIL: " + user.getEmail() +
                                "\n NAME: " + user.getDisplayName() +
                                "\n PROVIDER: " + user.getProviderData() +
                                "\n PHOTO: " + user.getPhotoUrl()
                        );


                        Toast.makeText(UserLoginActivity.this, "Registration Done.", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(UserLoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
//                            updateUI(null);
                    }

                    // ...
                });
    }

    public Uri getURLForResource(int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId);
    }

    private void setAdminName(FirebaseUser user) throws FileNotFoundException {
        StorageReference spaceRef = storageReference.child("Admin/" + user.getUid() + "/AdminPic.jpg");
        Uri uri = getURLForResource(R.drawable.final_jodi);

        spaceRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            spaceRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                Log.e(TAG, "updateUI: NAME TO BE SAVED: " + JODI_MILAN);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(JODI_MILAN)
                        .setPhotoUri(uri1)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.e(TAG, "User profile updated.\n" +
                                        "Email:" + user.getEmail() + "\n" +
                                        "Name: " + user.getDisplayName() + "\n" +
                                        "Photo URL: " + user.getPhotoUrl());
                                savedAdminToStorageDatabase(user.getPhotoUrl().toString());
                            }
                        });
            });
        }).addOnFailureListener(e -> Toast.makeText(UserLoginActivity.this, "Failed.", Toast.LENGTH_SHORT).show());
    }

    private void savedAdminToStorageDatabase(String userdp) {

        String uniqueID = UUID.randomUUID().toString();
        //Extracting user inputs
//        retrieveInputs();
        HashMap<String, Object> userNAME = new HashMap<>();

        //  Personal Info
        userNAME.put(FormDataVariables.bFullName, fAuth.getCurrentUser().getDisplayName());
        userNAME.put(FormDataVariables.bProfilePicture, userdp);
        userNAME.put(FormDataVariables.bUID, uniqueID);

        if (userNAME.size() > 0) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            // If not exists then create collection And then merge that data
            database.collection("Admin").document(fAuth.getCurrentUser().getUid()).set(userNAME, SetOptions.merge()).addOnCompleteListener(task -> {
                Intent intent = new Intent(UserLoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });


        }

    }

    public void loginWithEmail(View view) {
        String email = logEmail.getText().toString();
        String pass = logPass.getText().toString();
        if (email.length() > 0 && pass.length() > 0) {
            signInwithEmailPassword(email, pass);
        } else {
            Toast.makeText(this, "Invalid Credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInwithEmailPassword(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAGGY", "LoginWithEmail:success");
                        FirebaseUser user = fAuth.getCurrentUser();
//                        Toast.makeText(UserLoginActivity.this, "Email: " + user.getEmail() + "\n" +
//                                "Name: " + user.getDisplayName() + "\n" +
//                                "Photo: " + user.getPhotoUrl(), Toast.LENGTH_LONG).show();
//                        Picasso.get().load(user.getPhotoUrl()).into(profilePic);
                        Log.e(TAG, "signInwithEmailPassword: \n" +
                                "Name:" + user.getDisplayName() + "\n" +
                                "Photo: " + user.getPhotoUrl() + "\n" +
                                "Mobile: " + user.getPhoneNumber() + "\n"
                        );
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SharedPreferences sharedPreferences = getSharedPreferences(PrefVariables.LOGIN_STATS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(PrefVariables.ISLOGIN, true);
                        editor.putBoolean(PrefVariables.IS_REGISTERED, true);
                        editor.commit();
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "LoginWithEmail:failure", task.getException());
                        Toast.makeText(UserLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        // ...
                    }

                    // ...
                });

    }


    public void updatePassword(View view) {
        createAccount(ADMIN_EMAIL, "jodimilan4321");
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.login_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    public void forgotTextPass(View view) {
//        createAccount(ADMIN_EMAIL, "jodimilan4321");
        RelativeLayout relativeLayout=findViewById(R.id.relFogot);
        relativeLayout.setVisibility(View.VISIBLE);
        forgotEmail = findViewById(R.id.rel_forgot_email_edt);
        resLinkSet = findViewById(R.id.linksenttoEmail);

    }

    public void sentResetLink(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = forgotEmail.getText().toString();
        if(emailAddress!=null && !emailAddress.equals("")) {
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {Log.d(TAG, "Email sent.");
                        resLinkSet.setText("Reset link sent to above E-mail address.");
                    });

        }
    }
}