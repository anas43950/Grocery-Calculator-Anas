package com.grocerycalculator.utils;

import static com.grocerycalculator.utils.Constants.SharedPrefsConstants.SHARED_PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefsUtils {

    private SharedPrefsUtils() {
        throw new UnsupportedOperationException(
                "Should not create instance of Util class. Please use as static..");
    }

    static public boolean getBooleanData(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    static public int getIntData(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(key, 0);
    }

    static public long getLongData(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getLong(key, 900000);
    }

    static public String getStringData(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(key, null);
    }

    static public String getStringData(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    static public void saveData(Context context, String key, String val) {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit().putString(key, val).apply();
    }

    static public void saveData(Context context, String key, int val) {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, val).apply();
    }

    static public void saveData(Context context, String key, boolean val) {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, val)
                .apply();
    }

    static public void saveData(Context context, String key, long val) {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putLong(key, val)
                .apply();
    }


    static public SharedPreferences.Editor getSharedPrefEditor(Context context, String pref) {
        return context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit();
    }

    static public void saveData(Editor editor) {
        editor.apply();
    }
}
