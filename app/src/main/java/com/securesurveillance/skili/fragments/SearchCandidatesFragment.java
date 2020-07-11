package com.securesurveillance.skili.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.securesurveillance.skili.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCandidatesFragment extends Fragment {
Button btnSearch;
    public SearchCandidatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_searchcandidate, container, false);

        return  view;

    }

}
