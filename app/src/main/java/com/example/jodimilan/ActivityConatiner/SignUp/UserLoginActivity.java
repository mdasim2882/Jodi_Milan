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

    public static final int GALLERY_CODE = 1001;
    EditText logEmail, logPass;
    private FirebaseAuth fAuth;
    ImageView profilePic;
    StorageReference storageReference;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        setUpToolbar();

        fAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        logEmail = findViewById(R.id.email_login_edt);
        logPass = findViewById(R.id.login_pass_edt);
        profilePic = findViewById(R.id.profile_dp);

        profilePic.setOnClickListener(v -> {
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGallery, GALLERY_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
              //  profilePic.setImageURI(imageUri);
                FirebaseUser user = fAuth.getCurrentUser();
                if (user!=null) {
                    updateUI(user, imageUri);
                }
            }
        }
    }

    public void loginWithEmail(View view) {
        String email = logEmail.getText().toString();
        String pass = logPass.getText().toString();
        signInwithEmailPassword(email, pass);
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
                        Picasso.get().load(user.getPhotoUrl()).into(profilePic);
//                            updateUI(user, imageUri);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "LoginWithEmail:failure", task.getException());
                        Toast.makeText(UserLoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        // ...
                    }

                    // ...
                });

    }

    private void updateUI(FirebaseUser user, Uri imageUri) {
        StorageReference spaceRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/MyprofilePic.jpg");


        spaceRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserLoginActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                spaceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePic);
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName("Harry Potter")
                                .setPhotoUri(uri)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAGGY--->", "User profile updated.");
                                        }
                                    }
                                });





                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserLoginActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sgnInwithGoogleButton(View view) {
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.login_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}