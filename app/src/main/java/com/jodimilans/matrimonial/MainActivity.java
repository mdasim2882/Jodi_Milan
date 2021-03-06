package com.jodimilans.matrimonial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.jodimilans.matrimonial.ActivityConatiner.SignUp.LoginActivity;

import com.jodimilans.matrimonial.R;


public class MainActivity extends AppCompatActivity {

    public static final long SPLASH_SCREEN_TIME_OUT=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        findTimeUniqueId();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_main);
        //this will bind your MainActivity.class file with activity_main.
        Handler h = new Handler();
        h.postDelayed(() -> {
            Intent i = new Intent(MainActivity.this,
                    LoginActivity.class);
            //Intent is used to switch from one activity to another.

            startActivity(i);
            //invoke the SecondActivity.

            finish();
            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void findTimeUniqueId() {
        for(int i=1;i<=20;i++){
            System.out.println(System.currentTimeMillis());
        }
    }
}