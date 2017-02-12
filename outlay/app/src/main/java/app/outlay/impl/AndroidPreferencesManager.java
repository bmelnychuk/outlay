package app.outlay.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import app.outlay.core.data.AppPreferences;

public class AndroidPreferencesManager implements AppPreferences {
    private static final String PREF_FIRST_RUN = "_firstRun_v2";
    private static final String PREF_SESSION_ID = "_sessionId";

    private Context context;

    public AndroidPreferencesManager(Context context) {
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

//    private static void putInt(Context context, String key, int value) {
//        getPreferences(context).edit().putInt(key, value).apply();
//    }
//
//    private static int getInt(Context context, String key) {
//        return getPreferences(context).getInt(key, 0);
//    }

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

    public void putFirstRun(boolean value) {
        putBoolean(PREF_FIRST_RUN, value);
    }

    public boolean isFirstRun() {
        return getBoolean(PREF_FIRST_RUN, true);
    }

    @Override
    public void setFirstRun(boolean firstRun) {
        putFirstRun(firstRun);
    }

    @Override
    public String getSessionId() {
        return getString(PREF_SESSION_ID);
    }

    @Override
    public void setSessionId(String sessionId) {
        putString(PREF_SESSION_ID, sessionId);
    }
}
