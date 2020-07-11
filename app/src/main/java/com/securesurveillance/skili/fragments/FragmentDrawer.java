package com.securesurveillance.skili.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.MyApplication;
import com.securesurveillance.skili.OnBoardingActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.adapter.NavigationDrawerAdapter;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.ConnectivityUtils;
import com.securesurveillance.skili.listener.APIResponseInterface;
import com.securesurveillance.skili.listener.ClickListener;
import com.securesurveillance.skili.listener.FragmentDrawerListener;
import com.securesurveillance.skili.listener.ToolBarDrawerClickedListener;
import com.securesurveillance.skili.model.NavDrawerItem;
import com.securesurveillance.skili.profile.MyProfileActivity;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.RoundedImageView;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment implements ToolBarDrawerClickedListener {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private static String[] titles = null;
    RelativeLayout nav_header_container;
    // This is array of Images for drawer title image.
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    public static RoundedImageView imgProfile;
    private TextView tvUserName, tvUserEmail;
    private ImageView iv_Logout;
    SharePreferanceWrapperSingleton objSPS;

    public FragmentDrawer() {
    }


    public boolean isSliderOpen() {
        return mDrawerLayout.isDrawerOpen(containerView);
    }

    public List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<NavDrawerItem>();
        // Set drawer item.
        data.add(new NavDrawerItem("Home", R.drawable.slider_home));
        data.add(new NavDrawerItem("Notifications", R.drawable.slider_notification));
        // data.add(new NavDrawerItem("Search", R.drawable.slider_search));
        data.add(new NavDrawerItem("Favourites", R.drawable.slider_favourites));
        data.add(new NavDrawerItem("Jobs", R.drawable.slider_favourites));
        data.add(new NavDrawerItem("Chat History", R.drawable.slider_chat));

        data.add(new NavDrawerItem("Settings", R.drawable.slider_settings));
        data.add(new NavDrawerItem("Promote the app", R.drawable.slider_promote));
        data.add(new NavDrawerItem("About Us", R.drawable.slider_about));
        data.add(new NavDrawerItem("Feedback", R.drawable.slider_feedback));

        return data;
    }


    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // drawer labels
//        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        setParameter(layout);

        iv_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call logout api
                //callLogoutAPI();
                SharePreferanceWrapperSingleton.getSingletonInstance().setValueToSharedPref(Constants.LOGGED_IN, false);
                SharePreferanceWrapperSingleton.getSingletonInstance().setValueToSharedPref(Constants.SIGNEDUP, false);
                SharePreferanceWrapperSingleton.getSingletonInstance().setValueToSharedPref(Constants.ID, "");

                Intent i = new Intent(getContext(), OnBoardingActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
            }
        });
        return layout;
    }


    private void setParameter(View layout) {
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        nav_header_container = (RelativeLayout) layout.findViewById(R.id.nav_header_container);
        imgProfile = (RoundedImageView) layout.findViewById(R.id.imgProfile);
        tvUserEmail = (TextView) layout.findViewById(R.id.tvUserEmail);
        tvUserName = (TextView) layout.findViewById(R.id.tvUserName);
        if (!objSPS.getValueFromShared_Pref(Constants.IMAGE).equals("")) {
            Picasso.get().load(objSPS.getValueFromShared_Pref(Constants.IMAGE)).into(imgProfile, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    imgProfile.setBackground(getActivity().getDrawable(R.drawable.user_profile));
                }
            });
        }
        tvUserName.setText(CommonMethod.getFirstCapName(objSPS.getValueFromShared_Pref(Constants.FIRSTNAME)) + " " +
                CommonMethod.getFirstCapName(objSPS.getValueFromShared_Pref(Constants.LASTNAME)));
        tvUserEmail.setText(objSPS.getValueFromShared_Pref(Constants.MOBILE));
        iv_Logout = (ImageView) layout.findViewById(R.id.ivLogout);
        nav_header_container.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (ConnectivityUtils.isNetworkEnabled(getActivity())) {
                    onDrawerClicked(false);

                    Intent i = new Intent(getActivity(), MyProfileActivity.class);
                    i.putExtra("PROFILEID",objSPS.getValueFromShared_Pref(Constants.ID));
                    startActivity(i);
                } else {
                    CommonMethod.showToastMessage(getActivity(), "Please check your internet connection");
                }
            }
        });
        //adapter

        adapter = new

                NavigationDrawerAdapter(getContext(), R.layout.nav_drawer_row,

                getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new

                LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new

                RecyclerTouchListener(getActivity(), recyclerView, new

                ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        drawerListener.onDrawerItemSelected(view, position);
                        mDrawerLayout.closeDrawer(containerView);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        // nav_header_container.setBackgroundResource(getRandom());

    }

//    // This method use for change drawer image randomly on first time app load.
//    public int getRandom() {
//        int rand = new Random().nextInt(images.length);
//        return images[rand];
//
//    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onDrawerClicked(boolean openIt) {
        if (openIt) {
            mDrawerLayout.openDrawer(containerView);
        } else {
            mDrawerLayout.closeDrawer(containerView);

        }
    }


    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }


}
