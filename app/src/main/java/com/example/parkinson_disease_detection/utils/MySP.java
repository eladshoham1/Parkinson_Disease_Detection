package com.example.parkinson_disease_detection.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySP {

    public interface KEYS {
        public static final String MY_SP = "MY_SP";
        public static final String USER_ID = "USER_ID";
        public static final String USER_TYPE = "USER_TYPE";
    }

    private static MySP instance;
    private SharedPreferences prefs;

    public static MySP getInstance() {
        return instance;
    }

    private MySP(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(KEYS.MY_SP, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MySP(context);
        }
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String def) {
        return prefs.getString(key, def);
    }

    public void removeKey(String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key).apply();
    }
}