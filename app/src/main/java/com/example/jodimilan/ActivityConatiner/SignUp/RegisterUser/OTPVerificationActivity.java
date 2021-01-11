package com.example.jodimilan.ActivityConatiner.SignUp.RegisterUser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.jodimilan.ActivityConatiner.Body.PictureSetter;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerificationActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    public final String LOGIN_STATS = "loginJodiMilan";
    public final String ISLOGIN = "isLogin", MOBILE_NO = FormDataVariables.bMobile;


    EditText oTpEdt, mobileEdt;
    private boolean autoverifiedStatus, vfOnPressed, codeSentStatus;
    TextView enterno;

    TextInputLayout otpLayout, layoutmob;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationCodeBySystem;


    LinearLayout changeBox;
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
    private String gender;
    private String colour;
    private String body;
    private String mobnp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);

        initializeViews();
        setUpToolbar();
        Handler h=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Intent getter=getIntent();
                inputDob = getter.getStringExtra(FormDataVariables.bDoB);
                inputheight = getter.getStringExtra(FormDataVariables.bHeight);
                inputCountry = getter.getStringExtra(FormDataVariables.bCountry);
                inputState = getter.getStringExtra(FormDataVariables.bState);
                inputCity = getter.getStringExtra(FormDataVariables.bCity);
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
                colour = getter.getStringExtra(FormDataVariables.bColor);
                body = getter.getStringExtra(FormDataVariables.bBody);
                mobnp = getter.getStringExtra(FormDataVariables.bMobile);
            }
        };
        h.post(runnable);


    }
    private void setUpToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.otp_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initializeViews() {
        oTpEdt = findViewById(R.id.otp_edt);
        mobileEdt = findViewById(R.id.mobile_edt);
        enterno = findViewById(R.id.change_my_no);
        changeBox = findViewById(R.id.change_layout);
        otpLayout = findViewById(R.id.otp_ll);
        layoutmob = findViewById(R.id.mobile_ll);

        enterno.setOnClickListener(v -> {
            changeBox.setVisibility(View.VISIBLE);

        });
        initializeCallback();

        String getMob = getIntent().getStringExtra(MOBILE_NO);
        if (getMob != null) {
            sendVerificationCodeToUser(getMob);
        }


    }

    public void sendOTP(View view) {
        String input = mobileEdt.getText().toString();
        if (checkMobnoValidityInputs())
            sendVerificationCodeToUser(input);
    }

    public void verifyOTP(View view) {
        String input = oTpEdt.getText().toString();
        if (checkOTPnoValidityInputs()) {
            try {
                verifyCode(input);
            } catch (Exception e) {
                Toast.makeText(this, "Error occured: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    //-----------------------------------Authentication Panel-------------------------------------//
    public void verifyCode(String codeByUser) {
        // Show a Verifying... dialog
        Log.d(TAG, "verifyCode: VERIFYING . . . ");
        Toast.makeText(this, "VERIFYING . . . ", Toast.LENGTH_SHORT).show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);

    }

    private void initializeCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted: Automatically Verified " + credential);
                String code = credential.getSmsCode();
                /* Check if OTP is received on the current device is not null
                 *  Store it into the CommonUserInfoDetails.inputOTP
                 *  Verify it with PhoneAuth Credentials
                 * */
                String f = FirebaseAuth.getInstance().getUid();
                Log.d(TAG, "onVerificationCompleted: FUID " + f);
                if (code != null) {
                    String inputOTP = code;
                    Log.d(TAG, " RECEIVED OTP ON DEVICE: " + inputOTP);
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.d(TAG, "onVerificationFailed: called");
                if (codeSentStatus) {
                    autoverifiedStatus = false;
                }

                Log.d(TAG, "onVerificationFailed Called: LOG MESSAGE : \n" + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, token);
                Log.d(TAG, "onCodeSent: Called" + s);
                codeSentStatus = true;
                verificationCodeBySystem = s;
                autoverifiedStatus = false;
            }
        };
    }


    public void sendVerificationCodeToUser(String phoneNo) {
/*        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,   // Activity (for callback binding)
                mCallbacks);// OnVerificationStateChangedCallbacks*/

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }


    public boolean checkMobnoValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(mobileEdt.getText().toString())) {
            layoutmob.setErrorEnabled(true);
            layoutmob.setError("Invalid no.");
            contactStatus = false;
        } else {
            layoutmob.setErrorEnabled(false);
            layoutmob.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        autoverifiedStatus = true;
                        if (autoverifiedStatus) {
                            //Start the Welcome Fragments to the user
//                            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_STATS, Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean(ISLOGIN, true);
//                            editor.commit();
                            // Start Profile Picture Page Navigation View
                            setPictureAndCreateProfile();
                        }
                        String f = FirebaseAuth.getInstance().getUid();
                        Log.d(TAG, "onVerificationCompleted: FUID " + f);
                        Log.d(TAG, "onComplete: DONE WITH OTP VERIFICATION ");
                        showToaster("Verification Success");
                        vfOnPressed = true;


                    } else {
                        Log.d(TAG, "onComplete: FAILED LOG MESSAGE : " + task.getException().getMessage());
                        autoverifiedStatus = false;

                        Toast.makeText(OTPVerificationActivity.this, "Wrong OTP enterred", Toast.LENGTH_SHORT).show();


                    }


                });
    }

    private void setPictureAndCreateProfile() {
        String name=getIntent().getStringExtra(FormDataVariables.bFullName);
        Log.e(TAG, "Create Account: \n " +
                "\n EMAIL: "+inputEmailID+
                "\n NAME: "+inputFullName+
                "\n DIRECT_NAME: "+name+
                "\n MOBILE_NO: "+mobnp);


        Intent intent = new Intent(OTPVerificationActivity.this, PictureSetter.class);
        intent.putExtra(FormDataVariables.bGender, gender);
        intent.putExtra(FormDataVariables.bFathersName, inputFathersName);
        intent.putExtra(FormDataVariables.bFullName, inputFullName);
        intent.putExtra(FormDataVariables.bDoB, inputDob);
        intent.putExtra(FormDataVariables.bHeight, inputheight);
        intent.putExtra(FormDataVariables.bState, inputState);
        intent.putExtra(FormDataVariables.bCountry, inputCountry);
        intent.putExtra(FormDataVariables.bCity, inputCity);
        intent.putExtra(FormDataVariables.bAddress, inputAddress);
        intent.putExtra(FormDataVariables.bColor, colour);
        intent.putExtra(FormDataVariables.bBody, body);
        intent.putExtra(FormDataVariables.bEducation, inputEducation);
        intent.putExtra(FormDataVariables.bEmployedIn, inputEmployment);
        intent.putExtra(FormDataVariables.bOccupation, inputOccupation);
        intent.putExtra(FormDataVariables.bIncome, inputIncome);
        intent.putExtra(FormDataVariables.bMaritalStatus, inputMaritalStatus);
        intent.putExtra(FormDataVariables.bHaveChildren, inputHaveChildren);
        intent.putExtra(FormDataVariables.bMotherTongue, inputMotherTongue);
        intent.putExtra(FormDataVariables.bReligion, inputReligion);
        intent.putExtra(FormDataVariables.bEmail, inputEmailID);
        intent.putExtra(FormDataVariables.bPassword, inputPassword);
        intent.putExtra(FormDataVariables.bMobile, mobnp);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showToaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean checkOTPnoValidityInputs() {
        boolean otpStatus;
        if (TextUtils.isEmpty(oTpEdt.getText().toString())) {
            otpLayout.setErrorEnabled(true);
            otpLayout.setError("Invalid OTP");
            otpStatus = false;
        } else {

            otpLayout.setErrorEnabled(false);
            otpLayout.setError(null);
            otpStatus = true;
        }
        if (otpStatus == true)
            return true;
        return false;
    }

    //-----------------------Location Setup--------------------------------------------------//


}