package com.securesurveillance.skili.utility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.apiHandler.CommonMethod;


public class MarshMPermission {

    public static boolean checkPermission(Context mcontext, int permission) {
        int result = 1;
        if (!CommonMethod.isMarshMallow()) {
            return true;
        }
        switch (permission) {
//            case AppConstants.MPermission.M_READ_SMS:
//                result = ContextCompat.checkSelfPermission(mcontext, Manifest.permission.READ_SMS);
//                break;
//            case AppConstants.MPermission.M_CAMERA:
//                result = ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CAMERA);
//                break;
            case AppConstants.MPermission.M_SEND_SMS:
                result = ContextCompat.checkSelfPermission(mcontext, Manifest.permission.SEND_SMS);
                break;
            case AppConstants.MPermission.M_READ_EXTERNAL_STORAGE:
                result = ContextCompat.checkSelfPermission(mcontext, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
        }
        return result == PackageManager.PERMISSION_GRANTED;
    }

}
