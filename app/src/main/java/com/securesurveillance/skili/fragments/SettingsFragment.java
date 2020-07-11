package com.securesurveillance.skili.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.securesurveillance.skili.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rlChangePassword,rlNotification,rlAccountSettings;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        rlChangePassword = (RelativeLayout) view.findViewById(R.id.rlChangePassword);
        rlNotification = (RelativeLayout) view.findViewById(R.id.rlNotification);
        rlAccountSettings= (RelativeLayout) view.findViewById(R.id.rlAccountSettings);
        rlChangePassword.setOnClickListener(this);
        rlNotification.setOnClickListener(this);
        rlAccountSettings.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        switch (v.getId()) {
            case R.id.rlChangePassword:
               ChangePasswordFragment fragment = new ChangePasswordFragment();
                fragmentTransaction.replace(R.id.frameLayout, fragment).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.rlNotification:
                NotificationFragment fragmentNotification = new NotificationFragment();
                fragmentTransaction.replace(R.id.frameLayout, fragmentNotification).addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case R.id.rlAccountSettings:
                AccountSettingsFragment fragmentAccount = new AccountSettingsFragment();
                fragmentTransaction.replace(R.id.frameLayout, fragmentAccount).addToBackStack(null);
                fragmentTransaction.commit();

                break;
            default:
                break;
        }
    }
}
