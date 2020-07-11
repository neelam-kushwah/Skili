package com.securesurveillance.skili.fragments;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.securesurveillance.skili.BuildConfig;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.adapter.HomeGridViewAdapter;
import com.securesurveillance.skili.utility.ExpandableGridView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromoteAppFragment extends Fragment {

    ExpandableGridView gridView;

    public PromoteAppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promoteapp, container, false);
        gridView = (ExpandableGridView) view.findViewById(R.id.gridView);
        ArrayList<String> arrstr = new ArrayList<>();
        final ArrayList<String> arrPackage = new ArrayList<>();

        ArrayList<Drawable> arrImage = new ArrayList<>();
        PackageManager pm = getActivity().getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_SEND);
        mainIntent.setType("text/plain");
        mainIntent.putExtra(Intent.EXTRA_TEXT,"Find the Best Skilled Talent at Skili.\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID );
        mainIntent.putExtra(Intent.EXTRA_SUBJECT, "Skili");


        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0); // returns all applications which can listen to the SEND Intent
        for (ResolveInfo info : resolveInfos) {
            ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;

            //get package name, icon and label from applicationInfo object and display it in your custom layout
            arrImage.add(applicationInfo.loadIcon(pm));
            arrstr.add(applicationInfo.loadLabel(pm).toString());
            //App icon = applicationInfo.loadIcon(pm);
            //App name  = applicationInfo.loadLabel(pm).toString();
            arrPackage.add(applicationInfo.packageName);
            //App package name = applicationInfo.packageName;
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent waIntent = new Intent(Intent.ACTION_SEND);
//                waIntent.setType("text/plain");
//                String text = "This is  a Test"; // Replace with your own message.
//
//                PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//                //Check if package exists or not. If not then code
//                //in catch block will be called
                mainIntent.setPackage(arrPackage.get(i));

                startActivity(mainIntent);


            }
        });
        if(arrImage.size()>0){
            HomeGridViewAdapter adapter= new HomeGridViewAdapter(getActivity(),arrstr,arrImage, arrPackage);
            gridView.setAdapter(adapter);
            gridView.setNumColumns(3);
            gridView.setExpanded(true);
        }
        return view;
    }

}
