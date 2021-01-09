package com.example.jodimilan.ActivityConatiner.Body;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jodimilan.ActivityConatiner.SignUp.LoginActivity;
import com.example.jodimilan.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {
private final String TAG=getClass().getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth fAuth;
    private GoogleApiClient mGoogleApiClient;

    public final String LOGIN_STATS = "loginJodiMilan";
    public final String ISLOGIN = "isLogin";
    FloatingActionButton fabtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fAuth=FirebaseAuth.getInstance();
         fabtn= findViewById(R.id.fab);



        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername =headerView.findViewById(R.id.nameHeaderView);
        if (fAuth.getCurrentUser()!=null) {
            navUsername.setText(fAuth.getCurrentUser().getDisplayName());
            TextView navEmail =  headerView.findViewById(R.id.emailHeaderView);
            String s=fAuth.getCurrentUser().getEmail();
            if(s!=null) {
                navEmail.setText(s);
            }
        }


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_filter,R.id.nav_subscriptions,R.id.nav_privacyPolicy,R.id.nav_aboutUs)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        fabtn.setOnClickListener(v -> {
            Snackbar.make(v, "Set your search filter that matches your choice", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            navigationView.getMenu().getItem(2).setChecked(true);
            navigationView.setCheckedItem(R.id.nav_filter);

        });
    }
    public FloatingActionButton getFloatingActionButton() {
        return fabtn;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            //Goto Settings
            startActivity(new Intent(this,Settings.class));

        }
        else if (item.getItemId() == R.id.action_logout) {
            Log.d(TAG, "logout: Done");
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_STATS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(ISLOGIN, false);
            editor.commit();
            finish();
        fAuth.signOut();
        signOut();
        }return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}