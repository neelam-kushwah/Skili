package com.securesurveillance.skili.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.securesurveillance.skili.chat.ChatActivity;
import com.securesurveillance.skili.chat.ChatOneToOneActivity;
import com.securesurveillance.skili.model.responsemodel.NotificationModel;
import com.securesurveillance.skili.utility.Constants;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "SkiliFirebaseNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


            // Handle message within 10 seconds
            //  handleNow();


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        /*
         * If the device is having android oreo we will create a notification channel
         * */


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);

            mNotificationManager.createNotificationChannel(mChannel);


        }

        /*
         * Displaying a notification locally
         */
        boolean isFromChat = false;
        if (remoteMessage.getData().get("action").equals("CHAT")) {
            isFromChat = true;
        }
        if (ChatOneToOneActivity.chatActivity != null) {
            if (ChatOneToOneActivity.chatActivity.isFinishing()) {
                MyNotificationManager.getInstance(this).displayNotification(remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody(), isFromChat);
            } else {
                Gson gson = new Gson();
                if (remoteMessage.getData().get("action").equals("CHAT") &&
                        ChatOneToOneActivity.TO_PROFILE_ID.equalsIgnoreCase(remoteMessage.getData().get("tag1"))) {
                    sendMessageToActivity(remoteMessage.getData().get("tag1"), remoteMessage.getNotification().getBody());
                } else {
                    MyNotificationManager.getInstance(this).displayNotification(remoteMessage.getNotification().getTitle(),
                            remoteMessage.getNotification().getBody(), isFromChat);
                }
            }
        } else {

            MyNotificationManager.getInstance(this).displayNotification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(), isFromChat);
        }

    }

    private void sendMessageToActivity(String fromProfileId, String msg) {
        Intent intent = new Intent(ChatOneToOneActivity.chatReceiver);
        // You can also include some extra data.
        intent.putExtra("body", msg);
        intent.putExtra("fromProfileId", fromProfileId);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }


}
