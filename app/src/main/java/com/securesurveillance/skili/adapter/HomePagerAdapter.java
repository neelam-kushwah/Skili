package com.securesurveillance.skili.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.securesurveillance.skili.fragments.GlobalFeedFragment;
import com.securesurveillance.skili.fragments.HomeJobFragment;
import com.securesurveillance.skili.fragments.SliderJobFragment;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;


/**
 * Created by Hp on 9/7/2016.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {


    private final String[] TITLES ;
   // private final String[] TITLES1 = {"Recommended", "Post", "Posted"};

    String role = SharePreferanceWrapperSingleton.getSingletonInstance().getValueFromShared_Pref(Constants.ROLE);

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
          if (role.contains("Individual")) {
              TITLES = new String[]{"Jobs"};
          }else{
              TITLES = new String[]{"Global Feed"};

          }

//        this.isFromSLider = isFromSLider;
//        this.postAddress = postAddress;
//        this.postName = postName;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      //  if (role.contains("Individual")) {
            return TITLES[position];
//        } else {
//            return TITLES1[position];
//        }

    }

    @Override
    public int getCount() {
      //  if (role.contains("Individual")) {

            return TITLES.length;
//        } else {
//            return TITLES1.length;
//        }

    }

    @Override
    public Fragment getItem(int position) {
            if (TITLES[position].contains("Global")) {
                return new GlobalFeedFragment();
            } else {
                return new HomeJobFragment();
            }

    }
}
