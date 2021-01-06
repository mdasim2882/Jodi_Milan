package com.example.jodimilan.ActivityConatiner.Body;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.jodimilan.ActivityConatiner.SignUp.RegisterUser.RegisterActivity;
import com.example.jodimilan.ActivityConatiner.SignUp.UserLoginActivity;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.example.jodimilan.ActivityConatiner.SignUp.UserLoginActivity.GALLERY_CODE;

public class PictureSetter extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient
            mFusedLocationClient;
    int PERMISSION_ID = 44;
    ImageView profilePic;
    StorageReference storageReference;
    private FirebaseAuth fAuth;
    private FirebaseFirestore database;

    private String inputDob;
    private String inputheight;
    private String inputCountry;
    private String inputState;
    private String inputCity;
    private String inputEducation;
    private String inputEmployment;
    private String inputOccupation;
    private String inputIncome;
    private String inputMaritalStatus;
    private String inputHaveChildren;
    private String inputMotherTongue;
    private String inputReligion;
    private String inputFullName;
    private String inputFathersName;
    private String inputAddress;
    private String inputPassword;
    private String inputEmailID;
    private Uri imageUri;
    private String gender,body,colour,mobnp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_setter);
        mFusedLocationClient
                = LocationServices
                .getFusedLocationProviderClient(this);
        storageReference= FirebaseStorage.getInstance().getReference();
        profilePic = findViewById(R.id.profile_dp);
        fAuth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);


        profilePic.setOnClickListener(v -> {
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGallery, GALLERY_CODE);
        });
        // method to get the location
        getLastLocation();

        Runnable run= () -> {
            Intent getter=getIntent();
            inputDob = getter.getStringExtra(FormDataVariables.bDoB);
            inputheight = getter.getStringExtra(FormDataVariables.bHeight);
//            inputCountry = getter.getStringExtra(FormDataVariables.bCountry);
//            inputState = getter.getStringExtra(FormDataVariables.bState);
//            inputCity = getter.getStringExtra(FormDataVariables.bCity);
            inputEducation = getter.getStringExtra(FormDataVariables.bEducation);
            inputEmployment = getter.getStringExtra(FormDataVariables.bEmployedIn);
            inputOccupation = getter.getStringExtra(FormDataVariables.bOccupation);
            inputIncome = getter.getStringExtra(FormDataVariables.bIncome);
            inputMaritalStatus = getter.getStringExtra(FormDataVariables.bMaritalStatus);
            inputHaveChildren = getter.getStringExtra(FormDataVariables.bHaveChildren);
            inputMotherTongue =getter.getStringExtra(FormDataVariables.bMotherTongue);
            inputReligion = getter.getStringExtra(FormDataVariables.bReligion);
            inputFullName =getter.getStringExtra(FormDataVariables.bFullName);
            inputFathersName =getter.getStringExtra(FormDataVariables.bFathersName);
            inputAddress =getter.getStringExtra(FormDataVariables.bAddress);
            inputEmailID =getter.getStringExtra(FormDataVariables.bEmail);
            inputPassword =getter.getStringExtra(FormDataVariables.bPassword);

            gender = getter.getStringExtra(FormDataVariables.bGender);
            colour=getter.getStringExtra(FormDataVariables.bColor);
         body=getter.getStringExtra(FormDataVariables.bBody);
             mobnp=getter.getStringExtra(FormDataVariables.bMobile);
        };
        Handler h=new Handler();
        h.post(run);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                //  profilePic.setImageURI(imageUri);
                FirebaseUser user = fAuth.getCurrentUser();
                if (user!=null) {
                    Log.e(TAG, "onActivityResult: UID after authentication"+user.getUid() );
                    profilePic.setImageURI(imageUri);
//                    updateUI(user, imageUri);
                }
            }
        }
    }
    private void createAccount(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "createUserWithEmail:success");
                        FirebaseUser user = fAuth.getCurrentUser();
                        updateUI(user, imageUri,inputFullName);
                        Log.e(TAG, "Create Account: \n " +
                                "UID : "+user.getUid()+
                                "\n EMAIL: "+user.getEmail()+
                                "\n NAME: "+inputFullName+
                                "\n PROVIDER: "+user.getProviderData()+
                                "\n PHOTO: "+user.getPhotoUrl()+
                                "\n MOBILE_NO: "+mobnp);


                        Toast.makeText(PictureSetter.this, "Registration Done.", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(PictureSetter.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                    }

                    // ...
                });
    }


    private void updateUI(FirebaseUser user, Uri imageUri,String named) {
        StorageReference spaceRef = storageReference.child("USERS/"+fAuth.getCurrentUser().getUid()+"/MyPhotos.jpg");

        if (imageUri == null) {
            UserProfileChangeRequest nameUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName(named)
                    .build();

            user.updateProfile(nameUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                    String userdp=spaceRef.getDownloadUrl().toString();
                    savedToStorageDatabase(null);

                }
            });
        }else {
            spaceRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(PictureSetter.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                spaceRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                Picasso.get().load(uri).into(profilePic);
                    Glide.with(this).load(uri).into(profilePic);
                    Log.e(TAG, "updateUI: NAME TO BE SAVED: " + named);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(named)
                            .setPhotoUri(uri)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.e(TAG, "User profile updated.\n" +
                                            "Email:" + user.getEmail() + "\n" +
                                            "Name: " + user.getDisplayName() + "\n" +
                                            "Photo URL: " + user.getPhotoUrl());
                                    savedToStorageDatabase(user.getPhotoUrl().toString());
                                }
                            });


                });
            }).addOnFailureListener(e -> Toast.makeText(PictureSetter.this, "Failed.", Toast.LENGTH_SHORT).show());
        }

    }
    public void saveProfilePcture(View view) {
        progressDialog.setMessage("Creating Account... please wait");
        progressDialog.show();
        createAccount(inputEmailID,inputPassword);
    }

    //*****************************************************************************************//
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest
                = new LocationRequest();
        mLocationRequest.setPriority(
                LocationRequest
                        .PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient
                = LocationServices
                .getFusedLocationProviderClient(this);

        mFusedLocationClient
                .requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback,
                        Looper.myLooper());
    }
    private void showToaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private LocationCallback
            mLocationCallback
            = new LocationCallback() {

        @Override
        public void onLocationResult(
                LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            showToaster("Latitude: " + mLastLocation.getLatitude() + "\n" +
//                    "Longitude: " + mLastLocation.getLongitude());
            Log.e(TAG, "onLocationResult: \n" + "Latitude: " + mLastLocation.getLatitude() + "\n" +
                    "Longitude: " + mLastLocation.getLongitude());
        }
    };



    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission
                                .ACCESS_COARSE_LOCATION,
                        Manifest.permission
                                .ACCESS_FINE_LOCATION},
                PERMISSION_ID);
    }

    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(
                        this,
                        Manifest.permission
                                .ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED

                && ActivityCompat
                .checkSelfPermission(
                        this,
                        Manifest.permission
                                .ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        /* ActivityCompat
                .checkSelfPermission(
                    this,
                    Manifest.permission
                        .ACCESS_BACKGROUND_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        */
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient
                        .getLastLocation()
                        .addOnCompleteListener(
                                task -> {
                                    Location location = task.getResult();
                                    if (location == null) {
                                        requestNewLocationData();
                                    } else {
//                                        showToaster("\n" + "Country: " + location.getTime() + "\n" + "Latitude: " + location.getLatitude() + "\n"
//                                                + "Longitude: " + location.getLongitude());
                                        Log.e(TAG, "Latitude: " + location.getLatitude() + "\n"
                                                + "Longitude: " + location.getLongitude());

                                        try {
                                            getAddress(location.getLatitude(), location.getLongitude());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                        .show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager
                locationManager
                = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);

        return locationManager
                .isProviderEnabled(
                        LocationManager.GPS_PROVIDER)
                || locationManager
                .isProviderEnabled(
                        LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager
                    .PERMISSION_GRANTED) {

                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    public void getAddress(double lat, double lng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        FormDataVariables.gp=new GeoPoint(lat,lng);

        addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        inputCity=city;
        String state = addresses.get(0).getAdminArea();
        inputState=state;
        String country = addresses.get(0).getCountryName();
        inputCountry=country;
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        Log.e(TAG, "getAddress: Adress:\n" +
                        "City: " + city + "\n"
                        +"State: " + state + "\n"
                        +"Country: " + country + "\n"
                        +"Postal Code: " + postalCode + "\n"
                        + "Known name: " + knownName + "\n"
        );

    }
    private void savedToStorageDatabase(String userdp) {

        String uniqueID = UUID.randomUUID().toString();
        //Extracting user inputs
//        retrieveInputs();
            HashMap<String, Object> userNAME = new HashMap<>();

            //  Personal Info


            userNAME.put(FormDataVariables.bFullName, inputFullName);
            userNAME.put(FormDataVariables.bFathersName, inputFathersName);
            userNAME.put(FormDataVariables.bDoB, inputDob);
            userNAME.put(FormDataVariables.bHeight, inputheight);
            userNAME.put(FormDataVariables.bCountry, inputCountry.toUpperCase());
            userNAME.put(FormDataVariables.bCity, inputCity.toUpperCase());
            userNAME.put(FormDataVariables.bState, inputState.toUpperCase());
            userNAME.put(FormDataVariables.bAddress, inputAddress);
            userNAME.put(FormDataVariables.bColor, colour);
            userNAME.put(FormDataVariables.bGender, gender);

            userNAME.put(FormDataVariables.bGeopoint, FormDataVariables.gp);
            userNAME.put(FormDataVariables.bBody, body);
            userNAME.put(FormDataVariables.bEducation,inputEducation);
            userNAME.put(FormDataVariables.bEmployedIn, inputEmployment);
            userNAME.put(FormDataVariables.bOccupation, inputOccupation);
            userNAME.put(FormDataVariables.bIncome, inputIncome);

            userNAME.put(FormDataVariables.bMaritalStatus, inputMaritalStatus);
            userNAME.put(FormDataVariables.bHaveChildren, inputHaveChildren);
            userNAME.put(FormDataVariables.bMotherTongue, inputMotherTongue);
            userNAME.put(FormDataVariables.bReligion, inputReligion);

            userNAME.put(FormDataVariables.bMobile, mobnp);
            userNAME.put(FormDataVariables.bEmail, inputEmailID);
            userNAME.put(FormDataVariables.bUID, fAuth.getCurrentUser().getUid());
            userNAME.put(FormDataVariables.bProfilePicture, userdp);
            userNAME.put(FormDataVariables.bProfileID, uniqueID);



            if (userNAME.size() > 0) {
                // If not exists then create collection And then merge that data
                database.collection("Users").document(fAuth.getCurrentUser().getUid()).set(userNAME, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   Intent intent=new Intent(PictureSetter.this,HomeActivity.class);
                   startActivity(intent);
                   progressDialog.dismiss();
                    }
                });



            }

    }

}