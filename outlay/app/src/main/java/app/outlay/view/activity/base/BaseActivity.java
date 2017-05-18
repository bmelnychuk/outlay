package app.outlay.view.activity.base;

import android.view.View;

import app.outlay.App;
import app.outlay.analytics.Analytics;
import app.outlay.di.component.AppComponent;
import app.outlay.utils.ResourceManager;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public interface BaseActivity {
    App getApp();
    View getRootView();
    AppComponent getApplicationComponent();
    Analytics analytics();
    ResourceManager getResourceManager();
}
