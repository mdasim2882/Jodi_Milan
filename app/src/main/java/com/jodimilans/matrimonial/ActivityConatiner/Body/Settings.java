package com.jodimilans.matrimonial.ActivityConatiner.Body;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jodimilans.matrimonial.ActivityConatiner.SignUp.LoginActivity;
import com.jodimilans.matrimonial.ActivityConatiner.SignUp.UserLoginActivity;
import com.jodimilans.matrimonial.HelperClasses.FormDataVariables;
import com.jodimilans.matrimonial.HelperClasses.PrefVariables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jodimilans.matrimonial.R;

import java.util.HashMap;


public class Settings extends AppCompatActivity {
    static FirebaseFirestore database;
    private static String completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        database = FirebaseFirestore.getInstance();
        setUpToolbar();
        settingfragmentforSetting();
        //TODO: Use SharedPreferences to getDefault file and change SETTINGS using keys defined in setting_preferences.xml

    }

    private void settingfragmentforSetting() {
        getFragmentManager().beginTransaction().replace(R.id.settingsFragmLayout, new MyPrefenceFragment(this)).commit();
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.settings_app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @SuppressLint("ValidFragment")
    public static class MyPrefenceFragment extends PreferenceFragment {


        public static final String CREATE_AN_ACCOUNT = "Create an Account";
        public SwitchPreference notificationToggleButton;
        Activity activity;
        private AlertDialog.Builder builder;
        private DialogInterface.OnClickListener dialogClickListener;

        public MyPrefenceFragment(Activity activity) {
            this.activity = activity;
        }

        private void setAlertDialog() {
            dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        performDeleteAccountOperation();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;

                }
            };

            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirm");
            builder.setMessage("You won't be able to restore the data again.").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener);
            builder.show();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_preferences);
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            notificationToggleButton = (SwitchPreference) findPreference("notifications_status"); //Preference Key

            Preference myPref = findPreference("logout_button_preference");
            myPref.setOnPreferenceClickListener(preference -> {
                //TODO: Perform logout operations here
                if (myPref.getTitle() == CREATE_AN_ACCOUNT) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
//                    Toast.makeText(activity, "Show Dialog", Toast.LENGTH_SHORT).show();
                    setAlertDialog();
                }
                return true;
            });

            Preference loginAsAdmin = findPreference("admin_button_preference");
            loginAsAdmin.setOnPreferenceClickListener(preference -> {
                //TODO: Perform logout operations here

                    Intent intent = new Intent(getActivity(), UserLoginActivity.class);
                    intent.putExtra("requestAdmin",true);
                    startActivity(intent);
                return true;
            });



            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() == null) {
                myPref.setTitle(CREATE_AN_ACCOUNT);
            }
            Preference myPictureChanger = findPreference("dpChange_button_preference");
            myPictureChanger.setOnPreferenceClickListener(preference -> {
                //TODO: Perform logout operations here
                SharedPreferences sharedPreferences = activity.getSharedPreferences(PrefVariables.LOGIN_STATS, Context.MODE_PRIVATE);
                Boolean statusLogin = sharedPreferences.getBoolean(PrefVariables.IS_REGISTERED, false);
                if (firebaseAuth.getCurrentUser() != null && statusLogin) {
                    performGotoPictureSetter();

                } else {
                    Toast.makeText(activity, "Account not created yet.", Toast.LENGTH_SHORT).show();
                }
                return true;
            });

            sharedPrefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
                if (key.equals("notifications_status")) {
                    boolean test = sharedPreferences.getBoolean("notifications_status", false);
                    //Do whatever you want here. This is an example.
                    if (test) {
//                        testPref.setSummary("Enabled");
                        Log.d("SETTER", "onRegister: Enabled");
//                        testPref.setSummary("Enabled");
                        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                                .addOnCompleteListener(task -> {
                                    String msg = "Enabled";
                                    if (!task.isSuccessful()) {
                                        msg = "Disabled";
                                    }
                                    Log.d("SETTER SUB: ", msg);
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                });
                    } else {
//                        testPref.setSummary("Disabled");
                        Log.d("SETTER", "onRegister: Disabled");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("weather").addOnCompleteListener(task -> {
                            String msg = "Disabled";
                            if (!task.isSuccessful()) {
                                msg = "Enabled";
                            }
                            Log.d("SETTER SUB: ", msg);
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
//                if(key.equals("feedback_user")){
//                    String feedback = sharedPrefs.getString("feedback_user", "default_value");
//                    Log.d("Settings", "onCreate() called with: savedInstanceState = [" + feedback + "]");
//
//                }
            });
            EditTextPreference feedback_pref = (EditTextPreference) findPreference("feedback_user");
            if (feedback_pref != null) {
                String feedback = feedback_pref.getEditText().getText().toString();
                Log.d("Settings", "onCreate() called with: " + feedback);
                HashMap<String, Object> userNAME = new HashMap<>();
                userNAME.put(FormDataVariables.bFeedback, feedback);
                saveFeedback();
            }


        }

        private void saveFeedback() {

        }

        private void performGotoPictureSetter() {
            Intent intent = new Intent(getActivity(), PictureSetter.class);
            intent.putExtra("onlydp", true);
            startActivity(intent);
        }

        private void performDeleteAccountOperation() {
            database.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).delete().addOnCompleteListener(task -> {
                completed = "Settings";
                Log.d(completed, "onComplete() called with: task = [Completed ]");

            });
            SharedPreferences sharedPreferences = activity.getSharedPreferences(PrefVariables.LOGIN_STATS, Context.MODE_PRIVATE);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            completed=getClass().getSimpleName();
                            Log.d(completed, "User account deleted.");

                        }
                    });
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PrefVariables.ISLOGIN, false);
            editor.putBoolean(PrefVariables.IS_REGISTERED, false);
            editor.commit();
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

        @Override
        public void onResume() {
            super.onResume();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            boolean test = preferences.getBoolean("notifications_status", false);

            if (test) {
//                testPref.setSummary("Enabled");
                Log.d("SETTER", "onResume: Enabled");
            } else {
//                testPref.setSummary("Disabled");
                Log.d("SETTER", "onResume: Disabled");

            }
        }
    }
}