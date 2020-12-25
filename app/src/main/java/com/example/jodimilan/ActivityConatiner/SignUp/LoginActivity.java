package com.example.jodimilan.ActivityConatiner.SignUp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.jodimilan.R;
import com.example.jodimilan.ViewPagerAdapter.ZoomOutPageTransformer;
import com.example.jodimilan.ViewPagerAdapter.mViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
ViewPager viewPager;
String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Add a view pager for better UI/UX
        setMyViewPager();
    }
    private void setMyViewPager() {
        viewPager=findViewById(R.id.startview_pager);
        viewPager.setBackgroundColor(Color.TRANSPARENT);
        viewPager.setAdapter(new mViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setBackgroundColor(Color.TRANSPARENT);
        tabLayout.setupWithViewPager(viewPager, true);


    }

    public void setCurrentItem(int item, boolean smoothscroller) {
        viewPager.setCurrentItem(item, smoothscroller);
    }
}