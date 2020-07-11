package com.securesurveillance.skili.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.R;

/**
 * Created by adarsh on 9/30/2018.
 */

public class TutorialOneFragment extends Fragment {


    public TutorialOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.onboard_item, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView one = (ImageView) view.findViewById(R.id.iv_onboard);

        Picasso.get().load(R.drawable.tutorial1).fit().centerCrop().into(one);
    }
}