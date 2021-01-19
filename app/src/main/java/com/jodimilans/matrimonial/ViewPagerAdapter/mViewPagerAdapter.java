package com.jodimilans.matrimonial.ViewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jodimilans.matrimonial.ViewPagerAdapter.FragmentsStartUp.BeingDigital;
import com.jodimilans.matrimonial.ViewPagerAdapter.FragmentsStartUp.OffersShow;
import com.jodimilans.matrimonial.ViewPagerAdapter.FragmentsStartUp.YourChoice;


public class mViewPagerAdapter extends FragmentStatePagerAdapter {


    public mViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public mViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  OffersShow.getInstance();
            case 1:
                return BeingDigital.getInstance();
            case 2:
                return  YourChoice.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
