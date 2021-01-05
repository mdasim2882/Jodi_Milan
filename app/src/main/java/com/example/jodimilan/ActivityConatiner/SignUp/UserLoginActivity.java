package com.example.jodimilan.ActivityConatiner.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jodimilan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserLoginActivity extends AppCompatActivity {
private  final String TAG=getClass().getSimpleName();
    public static final int GALLERY_CODE = 1001;
    EditText logEmail, logPass;
    private FirebaseAuth fAuth;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        setUpToolbar();

        fAuth = FirebaseAuth.getInstance();
        logEmail = findViewById(R.id.email_login_edt);
        logPass = findViewById(R.id.login_pass_edt);


    }



    public void loginWithEmail(View view) {
        String email = logEmail.getText().toString();
        String pass = logPass.getText().toString();
        if (email.length()>0 && pass.length()>0) {
            signInwithEmailPassword(email, pass);
        }
        else{
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
                        Toast.makeText(UserLoginActivity.this, "Email: " + user.getEmail() + "\n" +
                                "Name: " + user.getDisplayName() + "\n" +
                                "Photo: " + user.getPhotoUrl(), Toast.LENGTH_LONG).show();
//                        Picasso.get().load(user.getPhotoUrl()).into(profilePic);
                        Log.e(TAG, "signInwithEmailPassword: \n" +
                                "Name:" + user.getDisplayName() + "\n" +
                                "Photo: " + user.getPhotoUrl()+"\n"+
                                "Mobile: " + user.getPhoneNumber()+"\n"
                        );
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



    public void sgnInwithGoogleButton(View view) {
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.login_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}