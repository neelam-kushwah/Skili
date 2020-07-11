package com.securesurveillance.skili.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.securesurveillance.skili.fragments.AppliedJobFragment;
import com.securesurveillance.skili.fragments.FavouriteJobFragment;
import com.securesurveillance.skili.fragments.GlobalFeedFragment;
import com.securesurveillance.skili.fragments.HomeJobFragment;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;


/**
 * Created by Hp on 9/7/2016.
 */
public class IndividualJobPagerAdapter extends FragmentPagerAdapter {


    private final String[] TITLES = {"Applied Jobs", "Favourites Job"};

    String role = SharePreferanceWrapperSingleton.getSingletonInstance().getValueFromShared_Pref(Constants.ROLE);

    public IndividualJobPagerAdapter(FragmentManager fm) {
        super(fm);


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
            if (position==0) {
                return new AppliedJobFragment();
            } else {
                return new FavouriteJobFragment();
            }

    }
}
