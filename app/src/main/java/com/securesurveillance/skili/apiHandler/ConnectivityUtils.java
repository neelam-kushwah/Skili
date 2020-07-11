package com.securesurveillance.skili.apiHandler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> <br/>
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 */
public class ConnectivityUtils {

    public static boolean isNetworkEnabled(Context pContext) {
        if (pContext != null) {
            try {
                NetworkInfo activeNetwork = getActiveNetwork(pContext);
                return activeNetwork != null && activeNetwork.isConnected();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static NetworkInfo getActiveNetwork(Context pContext) {
        ConnectivityManager conMngr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMngr == null ? null : conMngr.getActiveNetworkInfo();

    }

}