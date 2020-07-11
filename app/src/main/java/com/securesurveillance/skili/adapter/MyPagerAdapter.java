package com.securesurveillance.skili.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by adarsh on 9/30/2018.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragmentList = new ArrayList<>();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragments(Fragment fragment){
        fragmentList.add(fragment);
    }
}