package com.jodimilans.matrimonial.ActivityConatiner.Body;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.jodimilans.matrimonial.ActivityConatiner.Interfaces.LoadRegActivity;
import com.jodimilans.matrimonial.HelperClasses.FormDataVariables;
import com.jodimilans.matrimonial.HelperClasses.PrefVariables;
import com.jodimilans.matrimonial.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import com.jodimilans.matrimonial.ActivityConatiner.SignUp.UserLoginActivity;

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
    LoadRegActivity loadRegActivity;
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
    private String gender, body, colour, mobnp;
    ProgressDialog progressDialog;
    private boolean change;

    private Button createProfilebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_setter);
        setUpToolbar();
        storageReference = FirebaseStorage.getInstance().getReference();
        profilePic = findViewById(R.id.profile_dp);
        fAuth = FirebaseAuth.getInstance();

        database = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        createProfilebtn = findViewById(R.id.createProfilebtn);

        profilePic.setOnClickListener(v -> {
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGallery, UserLoginActivity.GALLERY_CODE);
        });
        // method to get the location
        mFusedLocationClient
                = LocationServices
                .getFusedLocationProviderClient(this);
        change = getIntent().getBooleanExtra("onlydp", false);
        Log.d(TAG, "onCreate: changed value: " + change);
        if (change) {
            createProfilebtn.setText("Update");
        } else {
            createProfilebtn.setText("Create Profile");
            getLastLocation();

            Runnable run = () -> {
                Intent getter = getIntent();
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
                inputMotherTongue = getter.getStringExtra(FormDataVariables.bMotherTongue);
                inputReligion = getter.getStringExtra(FormDataVariables.bReligion);
                inputFullName = getter.getStringExtra(FormDataVariables.bFullName);
                inputFathersName = getter.getStringExtra(FormDataVariables.bFathersName);
                inputAddress = getter.getStringExtra(FormDataVariables.bAddress);
                inputEmailID = getter.getStringExtra(FormDataVariables.bEmail);
                inputPassword = getter.getStringExtra(FormDataVariables.bPassword);

                gender = getter.getStringExtra(FormDataVariables.bGender);
                colour = getter.getStringExtra(FormDataVariables.bColor);
                body = getter.getStringExtra(FormDataVariables.bBody);
                mobnp = getter.getStringExtra(FormDataVariables.bMobile);
            };
            Handler h = new Handler();
            h.post(run);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserLoginActivity.GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                //  profilePic.setImageURI(imageUri);
                FirebaseUser user = fAuth.getCurrentUser();
                if (user != null) {
                    Log.e(TAG, "onActivityResult: UID after authentication" + user.getUid());
                    try {
                        profilePic.setImageURI(imageUri);
                    } catch (Exception e) {
                        showToaster("Size is too big to upload");
                    }
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
                        updateUI(user, imageUri, inputFullName, false);
                        SharedPreferences sharedPreferences = getSharedPreferences(PrefVariables.LOGIN_STATS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(PrefVariables.ISLOGIN, true);
                        editor.putBoolean(PrefVariables.IS_REGISTERED, true);
                        editor.commit();


                        UserInfo auth1user = user.getProviderData().get(0);
                        UserInfo authSeconduser = user.getProviderData().get(1);

                        String uid1 = auth1user.getUid();
                        String uid2 = authSeconduser.getUid();

                        String emailFirst = auth1user.getEmail();
                        String emailSecond = authSeconduser.getEmail();

                        String numberFirst = auth1user.getPhoneNumber();
                        String numberSecond = authSeconduser.getPhoneNumber();


                        Log.e(TAG, "Create Account: \n " +
                                "UID : " + fAuth.getUid() +
                                "Current User UID : " + user.getUid() +
                                "\n EMAIL: " + user.getEmail() +
                                "\n NAME: " + inputFullName +
                                "\n PROVIDER: " + user.getProviderData() +
                                "\n PHOTO: " + user.getPhotoUrl() +
                                "\n MOBILE_NO: " + mobnp +
                                "\n UID PROVIDERS--->" + user.getProviderData() +
                                "\n UID-1: " + uid1 +
                                "\n UID-2: " + uid2 +
                                "\n Email-1: " + emailFirst +
                                "\n Email-2: " + emailSecond +
                                "\n number-1: " + numberFirst +
                                "\n number-2: " + numberSecond
                        );


                        Toast.makeText(PictureSetter.this, "Registration Done.", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(PictureSetter.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
//                            updateUI(null);
                    }

                    // ...
                });
    }


    private void updateUI(FirebaseUser user, Uri imageUri, String named, boolean status) {
        StorageReference spaceRef = storageReference.child("USERS/" + user.getUid() + "/MyPhotos.jpg");

        if (imageUri == null) {


            UserProfileChangeRequest nameUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName(named)
                    .build();

            user.updateProfile(nameUpdate).addOnCompleteListener(task -> {
//                    String userdp=spaceRef.getDownloadUrl().toString();
                savedToStorageDatabase(null);

            });

        } else {
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
        if (change) {

            if (imageUri != null) {
                if (fAuth.getCurrentUser() != null) {
                    progressDialog.setMessage("Updating your picture...");
                    progressDialog.show();
                    updateUI();
                } else {
                    showToaster("Account not created yet.");
                }
            } else {
                showToaster("Image not selected");
            }
        } else {
            progressDialog.setMessage("Creating Account... please wait");
            progressDialog.show();
            createAccount(inputEmailID, inputPassword);
        }
    }

    private void updateUI() {
        StorageReference spaceRef = storageReference.child("USERS/" + fAuth.getCurrentUser().getUid() + "/MyPhotos.jpg");
        spaceRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(PictureSetter.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            spaceRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                Picasso.get().load(uri).into(profilePic);
                Glide.with(this).load(uri).into(profilePic);
                Log.e(TAG, "updateUI: PHOTO URL TO BE SAVED: " + uri);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build();
                FirebaseUser user = fAuth.getCurrentUser();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.e(TAG, "User profile updated.\n" +
                                        "Email:" + user.getEmail() + "\n" +
                                        "Name: " + user.getDisplayName() + "\n" +
                                        "Photo URL: " + user.getPhotoUrl());
                                updateToStorageDatabase(user.getPhotoUrl().toString());
                            }
                        });


            });
        }).addOnFailureListener(e -> Toast.makeText(PictureSetter.this, "Failed.", Toast.LENGTH_SHORT).show());
    }
    private void setUpToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.set_profile_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
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
        if (!change) {
            if (checkPermissions()) {
                getLastLocation();
            }
        }
    }

    public void getAddress(double lat, double lng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        FormDataVariables.gp = new GeoPoint(lat, lng);

        addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        inputCity = city;
        String state = addresses.get(0).getAdminArea();
        inputState = state;
        String country = addresses.get(0).getCountryName();
        inputCountry = country;
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        Log.e(TAG, "getAddress: Adress:\n" +
                "City: " + city + "\n"
                + "State: " + state + "\n"
                + "Country: " + country + "\n"
                + "Postal Code: " + postalCode + "\n"
                + "Known name: " + knownName + "\n"
        );

    }
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    private void savedToStorageDatabase(String userdp) {

//        String uniqueID = UUID.randomUUID().toString();
        String uniqueID = getRandomNumberString();
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
        userNAME.put(FormDataVariables.bEducation, inputEducation);
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
            database.collection("Users").document(fAuth.getCurrentUser().getUid()).set(userNAME, SetOptions.merge()).addOnCompleteListener(task -> {
                Intent intent = new Intent(PictureSetter.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                progressDialog.dismiss();
            });


        }

    }


    private void updateToStorageDatabase(String image) {
        HashMap<String, Object> userNAME = new HashMap<>();
        userNAME.put(FormDataVariables.bProfilePicture, image);
        if (userNAME.size() > 0) {
            // If not exists then create collection And then merge that data
            database.collection("Users").document(fAuth.getCurrentUser().getUid()).set(userNAME, SetOptions.merge()).addOnCompleteListener(task -> {
                finish();
                progressDialog.dismiss();
            });


        }

    }


}