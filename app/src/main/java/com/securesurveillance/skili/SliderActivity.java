package com.securesurveillance.skili;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.securesurveillance.skili.chat.ChatActivity;
import com.securesurveillance.skili.chat.ChatOneToOneActivity;
import com.securesurveillance.skili.utility.SweetAlertController;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.fragments.FavouriteFragment;
import com.securesurveillance.skili.fragments.FeedbackFragment;
import com.securesurveillance.skili.fragments.FragmentDrawer;
import com.securesurveillance.skili.fragments.HomeFragment;
import com.securesurveillance.skili.fragments.IndividualJobFragment;
import com.securesurveillance.skili.fragments.NewAboutUsFragment;
import com.securesurveillance.skili.fragments.NewSettingsFragment;
import com.securesurveillance.skili.fragments.PromoteAppFragment;
import com.securesurveillance.skili.fragments.SkilliHomeFragment;
import com.securesurveillance.skili.fragments.SliderJobFragment;
import com.securesurveillance.skili.listener.FragmentDrawerListener;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SliderActivity extends AppCompatActivity implements FragmentDrawerListener, View.OnClickListener {

    private static String TAG = SliderActivity.class.getSimpleName();
    private FragmentDrawer drawerFragment;
    private Toolbar toolBar;
    private boolean isFromNotification = false;
    private TextView tvTitle;
    public static ImageView ivMenu, ivRight;
    SharePreferanceWrapperSingleton objSPS;
    public static TextView hotlist_hot;
    ContextMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(this);
        if (getIntent().hasExtra("ISFROMNOTI")) {
            isFromNotification = true;
        }
        setParameter();

    }


    private void setParameter() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        hotlist_hot = (TextView) findViewById(R.id.hotlist_hot);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolBar);
        drawerFragment.setDrawerListener(this);
        if (getIntent().hasExtra("SCREEN") && getIntent().getStringExtra("SCREEN").equals("JOBS")) {
            displayView(3);
        } else {
            displayView(0);
            if (isFromNotification && !getIntent().getBooleanExtra("ACTION", false)) {
                Intent i = new Intent(SliderActivity.this, NotificationActivity.class);
                startActivity(i);
            } else if (getIntent().getBooleanExtra("ACTION", false)) {
                Intent i = new Intent(SliderActivity.this, ChatActivity.class);

                if (getIntent().hasExtra("NAME") && !TextUtils.isEmpty(getIntent().getStringExtra("NAME"))) {
                    i.putExtra("TOPROFILEID", getIntent().getStringExtra("TOPROFILEID"));
                    i.putExtra("NAME", getIntent().getStringExtra("NAME"));
                    i.putExtra("PIC", getIntent().getStringExtra("PIC"));
                }
                startActivity(i);

            }
        }
        ivMenu.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String s = getString(R.string.app_name);
        String tag = "str";
        String role = objSPS.getValueFromShared_Pref(Constants.ROLE);
        FavouriteFragment.isVertical = false;
        switch (position) {
            case 0:
                s = "Home";
                fragment = new SkilliHomeFragment();
                // done
                break;
            case 1:
                fragment = null;
                Intent i = new Intent(SliderActivity.this, NotificationActivity.class);
                startActivity(i);


                break;

            case 2:

// ques:  where to  add favourite recruiter

                // favourite jobs and favourites candidate
                s = "Favourites";
                fragment = new FavouriteFragment();
                // for recruiter, show candidate list and for individual show recruiter list
// done, inidividual api integration remaining
                break;
            case 3:
                s = "Jobs";
                if (role.contains("Individual")) {
                    fragment = new IndividualJobFragment();
                } else {
                    fragment = new SliderJobFragment();// for recruiter, this will call my posted jobs and for individual , this will be the applied jobs
                    // done
                }
                break;
            case 4:
                fragment = null;
                i = new Intent(SliderActivity.this, ChatActivity.class);
                startActivity(i);

                break;
            case 5:
                s = "Settings";
                fragment = new NewSettingsFragment();
                break;
            case 6:
                s = "Promote the app";
                fragment = new PromoteAppFragment();
                break;
            case 7:
                s = "About Us";
                fragment = new NewAboutUsFragment();
                break;
            case 8:
                s = "Support & Feedback";
                fragment = new FeedbackFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment, s).addToBackStack(null);

            if (fragment instanceof HomeFragment) {
                fragmentTransaction.detach(fragment);
                fragmentTransaction.attach(fragment);
            }


            // set the toolbar title
            fragmentTransaction.commit();
            setRightIcon(fragment);
            tvTitle.setText(s);
        }
    }

    private void openSliderMenu(boolean val) {

        FragmentDrawer fragmentDrawer = (FragmentDrawer) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_Tag));
        fragmentDrawer.onDrawerClicked(val);
    }

    private boolean isSliderMenuOpened() {
        FragmentDrawer fragmentDrawer = (FragmentDrawer) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_Tag));
        return fragmentDrawer.isSliderOpen();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMenu:
                openSliderMenu(true);
                break;
            case R.id.ivRight:
                FavouriteFragment myFragment = (FavouriteFragment) getSupportFragmentManager().findFragmentByTag("Favourites");
                if (myFragment != null && myFragment.isVisible()) {
                    // add your code here
                    if (!FavouriteFragment.isVertical) {
                        FavouriteFragment.isVertical = true;
                        Picasso.get().load(R.drawable.fav_horizontal).into(ivRight);


                    } else {
                        FavouriteFragment.isVertical = false;
                        Picasso.get().load(R.drawable.fav_vertical).into(ivRight);

                    }
                    myFragment.changeAdapterView();
                } else {
                    Intent i = new Intent(SliderActivity.this, NotificationActivity.class);
                    startActivity(i);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (isSliderMenuOpened()) {
            openSliderMenu(false);
        } else {
            Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            if (fragment1 instanceof SkilliHomeFragment) {

                SweetAlertController.getInstance().showExitDialog(SliderActivity.this, SweetAlertDialog.WARNING_TYPE);

            } else {
                Fragment fragment = new SkilliHomeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment, "Home").addToBackStack(null);

                if (fragment instanceof HomeFragment) {
                    fragmentTransaction.detach(fragment);
                    fragmentTransaction.attach(fragment);
                }

                // set the toolbar title
                fragmentTransaction.commit();
                tvTitle.setText("Home");
                Picasso.get().load(R.drawable.notification).into(ivRight);
            }

//            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//
//                finish();
//            } else {
//
//                super.onBackPressed();
//                setRightIcon(getSupportFragmentManager().findFragmentById(R.id.frameLayout));
//            }
        }
    }

    private void setRightIcon(Fragment fragment) {
        //   FavouriteFragment myFragment = (FavouriteFragment) getSupportFragmentManager().findFragmentByTag("Favourites");
        if (fragment instanceof FavouriteFragment) {
            hotlist_hot.setVisibility(View.GONE);

            tvTitle.setText("Favourites");
            Picasso.get().load(R.drawable.fav_vertical).into(ivRight);
        }
        //   SkilliHomeFragment myFragment1 = (SkilliHomeFragment) getSupportFragmentManager().findFragmentByTag("Home");
        if (fragment instanceof SkilliHomeFragment) {
            tvTitle.setText("Home");

            Picasso.get().load(R.drawable.notification).into(ivRight);
        }
        if (objSPS.getValueFromShared_Pref(Constants.ROLE).equals("Individual")) {
            // IndividualJobFragment myFragment2 = (IndividualJobFragment) getSupportFragmentManager().findFragmentByTag("Jobs");
            if (fragment instanceof IndividualJobFragment) {
                tvTitle.setText("Jobs");

                Picasso.get().load(R.drawable.notification).into(ivRight);
            }
        } else {
            //   SliderJobFragment myFragment2 = (SliderJobFragment) getSupportFragmentManager().findFragmentByTag("Jobs");
            if (fragment instanceof SliderJobFragment) {
                tvTitle.setText("Jobs");

                Picasso.get().load(R.drawable.notification).into(ivRight);
            }
        }

        //  NewSettingsFragment myFragment3 = (NewSettingsFragment) getSupportFragmentManager().findFragmentByTag("Settings");
        if (fragment instanceof NewSettingsFragment) {
            tvTitle.setText("Settings");

            Picasso.get().load(R.drawable.notification).into(ivRight);
        }


        //PromoteAppFragment myFragment4 = (PromoteAppFragment) getSupportFragmentManager().findFragmentByTag("Promote the app");
        if (fragment instanceof PromoteAppFragment) {
            tvTitle.setText("Promote the app");

            Picasso.get().load(R.drawable.notification).into(ivRight);
        }

        // NewAboutUsFragment myFragment5 = (NewAboutUsFragment) getSupportFragmentManager().findFragmentByTag("About Us");
        if (fragment instanceof NewAboutUsFragment) {
            tvTitle.setText("About Us");

            Picasso.get().load(R.drawable.notification).into(ivRight);
        }
        //  FeedbackFragment myFragment6 = (FeedbackFragment) getSupportFragmentManager().findFragmentByTag("Support & Feedback");
        if (fragment instanceof FeedbackFragment) {
            tvTitle.setText("Support & Feedback");

            Picasso.get().load(R.drawable.notification).into(ivRight);
        }


    }
}
