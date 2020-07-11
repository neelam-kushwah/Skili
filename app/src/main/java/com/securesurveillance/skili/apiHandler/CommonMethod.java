package com.securesurveillance.skili.apiHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.securesurveillance.skili.BuildConfig;
import com.securesurveillance.skili.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonMethod {
    static int pos = -1;
    private static String TAG = CommonMethod.class.getSimpleName();
    private static SimpleDateFormat sDateFormat, simpleDate;

    public static boolean isMarshMallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static String convertToCamelCase(String x) {
        if (!x.isEmpty()) {
            Scanner sc = new Scanner(x);
            String out = "", temp;
            while (sc.hasNext()) {
                temp = sc.next();
                temp = temp.substring(0, 1).toUpperCase() + temp.substring(1, temp.length()).toLowerCase();
                out = out + temp + " ";
            }
            return out;
        } else {
            return x;
        }
    }
    public static String getFirstCapName(String line){
        String upper_case_line = "";
        Scanner lineScan = new Scanner(line);
        while(lineScan.hasNext()) {
            String word = lineScan.next();
            if(String.valueOf(word.charAt(0)).matches("^[a-zA-Z]*$")) {
                upper_case_line += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
            }else{
                upper_case_line +=word +" ";
            }
        }
        return upper_case_line.trim();
    }

    public static void disbaleScreenshots(Activity activity) {
        //activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public static void setLocale(Context ctx, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = ctx.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static String twoDecimalPlace(String inputStr) {
        double gTotal = Double.parseDouble(inputStr);
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(gTotal);
    }

    public static void showToastMessage(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    public static void printInfoLog(String tag, String str) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, str);
        }
    }

    public static void printErrorLog(String tag, String str) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, str);
        }
    }

    public static void printDebugLog(String tag, String str) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, str);
        }
    }

    public static boolean checkPasswordLength(String s, int digitNo) {
        return s != null && s.length() < digitNo;
    }

    public static void loadingDialog(ProgressDialog dialog, Context context) {
        if (context == null)
            return;
        Activity activity = (Activity) context;
        if (!activity.isFinishing()) {
            if (dialog != null) {
                dialog.setTitle(context.getResources().getString(R.string.app_name));
                dialog.setMessage("Please wait..");
                dialog.setProgressStyle(R.drawable.dialog_loader);
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

    public static void cancelDialog(ProgressDialog dialog, Context context) {
        if (context == null)
            return;
        try {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void printAPIResponse(Object response) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(response);
            CommonMethod.printInfoLog(TAG, " Response " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showApiMsgToast(Context context, int code) {
        if (context != null) {
            printErrorLog(TAG, " Response code " + code);
            if (code == 400) {
                showToastMessage(context, context.getString(R.string.bad_Request) + " " + code);
            } else if (code == 401) {
                showToastMessage(context, context.getString(R.string.unauthorized_user) + " " + code);
            } else if (code == 404) {
                showToastMessage(context, context.getString(R.string.page_not_found) + " " + code);
            } else if (code >= 500) {
                showToastMessage(context, context.getString(R.string.something_went_wrong_on_server) + " " + code);
            } else {
                showToastMessage(context, context.getString(R.string.something_went_wrong) + " " + code);
            }
        }
    }

    public static boolean checkNumberDigits(String mobile, int digitValue) {
        return !(mobile.isEmpty() || mobile.length() != digitValue);
    }

    public static boolean checkNumberStart987(String mobile, int digitValue) {
        return !(mobile.isEmpty() || mobile.length() != digitValue) && (mobile.startsWith("9") || mobile.startsWith("8") || mobile.startsWith("7"));
    }


}
