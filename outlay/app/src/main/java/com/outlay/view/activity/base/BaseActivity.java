package com.outlay.view.activity.base;

import android.view.View;

import com.outlay.App;
import com.outlay.analytics.Analytics;
import com.outlay.di.component.AppComponent;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public interface BaseActivity {
    App getApp();
    View getRootView();
    AppComponent getApplicationComponent();
    Analytics analytics();
}
