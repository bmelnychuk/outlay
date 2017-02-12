package app.outlay.impl;

import android.util.Log;

import app.outlay.core.logger.Logger;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class AndroidLogger implements Logger {
    private static final String TAG = "Outlay";

    @Override
    public void info(String message) {
        Log.i(TAG, message);
    }

    @Override
    public void warn(String message) {
        Log.w(TAG, message);
    }

    @Override
    public void warn(String message, Throwable e) {
        Log.w(TAG, message, e);
    }

    @Override
    public void debug(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void error(String message) {
        Log.e(TAG, message);
    }

    @Override
    public void error(String message, Throwable e) {
        Log.e(TAG, message, e);
    }
}
