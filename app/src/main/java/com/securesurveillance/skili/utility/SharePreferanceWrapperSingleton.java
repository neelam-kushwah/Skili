package com.securesurveillance.skili.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Neelam Kushwah
 */
@SuppressLint({"CommitPrefEdits", "NewApi"})
public class SharePreferanceWrapperSingleton {

    private static SharedPreferences pref;
    private static SharePreferanceWrapperSingleton singleton;
    private Editor editor;

    /*
     * A private Constructor prevents any other class from instantiating.
     */
    private SharePreferanceWrapperSingleton() {
    }

    /* Static 'instance' method */
    public static SharePreferanceWrapperSingleton getSingletonInstance() {

        if (null == singleton) {
            singleton = new SharePreferanceWrapperSingleton();

        }
        return singleton;
    }

    public int getValueFromSharedPref(String key) {

        return pref.getInt(key, 0);
    }

    public void setValueToSharedPref(String key, int value) {
        editor.putInt(key, value);
        editor.commit();

    }

    public void setValueToSharedPref(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }

    public String getValueFromShared_Pref(String key) {

        return pref.getString(key, "");
    }

    public void setValueToSharedPref(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();

    }

    public void setValueToSharedPrefLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();

    }

    public boolean getBoolValueFromSharedPref(String key) {

        return pref.getBoolean(key, false);
    }

    public long getLongValueFromSharedPref(String key) {

        return pref.getLong(key, 0);
    }


    @SuppressWarnings("static-access")
    public void setPref(Context context) {
        this.pref = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
        setEditor();
    }

    public void setEditor() {
        this.editor = pref.edit();
        this.editor.commit();

    }

    public void clearEditor() {
        this.editor = pref.edit();
        this.editor.clear();
        this.editor.commit();

    }

}
