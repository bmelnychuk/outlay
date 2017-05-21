package app.outlay.view.activity.base;

import android.view.View;

import app.outlay.App;
import app.outlay.analytics.Analytics;
import app.outlay.di.component.AppComponent;
import app.outlay.impl.AppPreferences;
import app.outlay.utils.ResourceHelper;
import app.outlay.view.OutlayTheme;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public interface BaseActivity {
    App getApp();
    View getRootView();
    OutlayTheme getOutlayTheme();
    AppComponent getApplicationComponent();
    Analytics analytics();
    ResourceHelper getResourceHelper();
    AppPreferences appPreferences();
}
