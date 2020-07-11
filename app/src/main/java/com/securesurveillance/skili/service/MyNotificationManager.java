package com.securesurveillance.skili.service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.securesurveillance.skili.LauncherActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.utility.Constants;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body,boolean isFromChat) {
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                        .setContentTitle(title)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setLights(mCtx.getResources().getColor(R.color.colorAccent),1000,1500)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH);



        Intent resultIntent = new Intent(mCtx, LauncherActivity.class);
        resultIntent.putExtra("ISFROMNOTI",true);
        resultIntent.putExtra("ACTION",isFromChat);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);


        if (mNotifyMgr != null) {
            mNotifyMgr.notify(new Random().nextInt(54367), mBuilder.build());
        }
    }

}