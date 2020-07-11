package com.securesurveillance.skili.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.securesurveillance.skili.OnBoardingActivity;
import com.securesurveillance.skili.fragments.NewsFragment;


/**
 * Created by Hp on 9/7/2016.
 */
public class SavedPagerAdapter extends FragmentPagerAdapter {


    private final String[] TITLES = {"Saved","Favourites"};


    public SavedPagerAdapter(FragmentManager fm) {
        super(fm);
//        this.isFromSLider = isFromSLider;
//        this.postAddress = postAddress;
//        this.postName = postName;
    }

    @Override
    public CharSequence getPageTitle(int position) {
            return TITLES[position];

    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NewsFragment();
        } else {
              return new NewsFragment();
        }

    }
}
