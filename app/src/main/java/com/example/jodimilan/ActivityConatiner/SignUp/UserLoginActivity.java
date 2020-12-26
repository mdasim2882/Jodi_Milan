package com.example.jodimilan.ActivityConatiner.SignUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.jodimilan.R;

public class UserLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        setUpToolbar();
    }

    public void loginWithEmail(View view) {
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