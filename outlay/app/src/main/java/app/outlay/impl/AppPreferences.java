package app.outlay.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import app.outlay.BuildConfig;

public class AppPreferences {
    public static final int THEME_DARK = 0;
    public static final int THEME_LIGHT = 1;

    private static final String PREF_THEME = "_outlayTheme";

    private Context context;

    public AppPreferences(Context context) {
        this.context = context;
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void putString(String key, String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    private String getString(String key) {
        return getPreferences().getString(key, null);
    }

    private void putInt(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    private int getInt(String key) {
        return getPreferences().getInt(key, 0);
    }

    private void putBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    private boolean getBoolean(String key) {
        return getPreferences().getBoolean(key, false);
    }

    private boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public void clear() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear().commit();
    }

    public void setTheme(int theme) {
        putInt(PREF_THEME, theme);
    }

    public int getTheme() {
        return getInt(PREF_THEME);
    }

    public boolean showWhatsNew() {
        String key = BuildConfig.VERSION_NAME + "_whatsNew";
        boolean result = getBoolean(key, true);
        if (result) {
            putBoolean(key, false);
        }
        return result;
    }

}
