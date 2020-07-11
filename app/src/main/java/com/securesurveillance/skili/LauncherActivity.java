package com.securesurveillance.skili;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;

import java.util.Iterator;
import java.util.Set;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by adarsh on 6/1/2018.
 */

public class LauncherActivity extends Activity {
    SharePreferanceWrapperSingleton objSPS;
    PulsatorLayout pulsator;
    boolean isFromNotification = false;
    boolean isChat = false;
    String name = "", pic = "", tag1="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(LauncherActivity.this);
        pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();
        executeFunctionality(getIntent());

    }
private void executeFunctionality(Intent intent){
    NotificationManager mNotifyMgr =
            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    mNotifyMgr.cancelAll();
    if (intent.hasExtra("google.delivered_priority") || intent.hasExtra("ISFROMNOTI")) {
        isFromNotification = true;
        Bundle bundle = intent.getExtras();


        if (intent.hasExtra("action") && intent.getStringExtra("action").equals("CHAT")) {
            isChat = true;
            if (bundle != null) {
                tag1=bundle.getString("tag1");
                name = bundle.getString("tag2");
                pic = bundle.getString("tag3");
            }
        } else if (intent.getBooleanExtra("ACTION", false)) {
            isChat = true;

        }


        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                Log.e("NOTI","[" + key + "=" + bundle.get(key)+"]");
            }
        }
    }

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent i;
            if (SharePreferanceWrapperSingleton.getSingletonInstance().getBoolValueFromSharedPref(Constants.LOGGED_IN)) {
                i = new Intent(LauncherActivity.this, SliderActivity.class);
                if (isFromNotification) {
                    i.putExtra("ISFROMNOTI", isFromNotification);
                    i.putExtra("ACTION", isChat);
                    if (!TextUtils.isEmpty(name)) {
                        i.putExtra("NAME", name);
                        i.putExtra("PIC", pic);
                        i.putExtra("TOPROFILEID", tag1);
                        Log.e("NAME",name+" "+ pic);

                    }
                }

            } else {
                i = new Intent(LauncherActivity.this, OnBoardingActivity.class);
            }
            startActivity(i);
            finish();
        }
    };
    handler.postDelayed(r, 3000);

}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        executeFunctionality(intent);
    }
}
