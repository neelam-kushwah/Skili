package com.securesurveillance.skili.fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.securesurveillance.skili.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewAboutUsFragment extends Fragment implements View.OnClickListener {
    ImageView ivFb, ivEmail, ivInsta, ivTwitter;

    public NewAboutUsFragment() {
        // Required empty public constructor
    }

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        context = getActivity();


        ivTwitter = (ImageView) view.findViewById(R.id.ivTwitter);
        ivFb = (ImageView) view.findViewById(R.id.ivFb);
        ivEmail = (ImageView) view.findViewById(R.id.ivEmail);
        ivInsta = (ImageView) view.findViewById(R.id.ivInsta);
        ivInsta.setVisibility(View.GONE);
        ivEmail.setOnClickListener(this);
        ivFb.setOnClickListener(this);
        ivInsta.setOnClickListener(this);
        ivTwitter.setOnClickListener(this);
        return view;
    }

    public static String FACEBOOK_URL = "https://www.facebook.com/Skili-2188667641450248";
    public static String FACEBOOK_PAGE_ID = "Skili-2188667641450248";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL() {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivTwitter:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=skili56784132")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/skili56784132")));
                }
                break;
            case R.id.ivInsta:
                Uri uri = Uri.parse("https://www.instagram.com/kartikaaryan/?hl=en");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/kartikaaryan/?hl=en")));
                }
                break;
            case R.id.ivEmail:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:startupskili18@gmail.com"));
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;

            case R.id.ivFb:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL();
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
                break;
        }
    }
}
