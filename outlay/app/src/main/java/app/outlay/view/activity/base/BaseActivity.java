package app.outlay.view.activity.base;

import android.view.View;

import app.outlay.App;
import app.outlay.analytics.Analytics;
import app.outlay.di.component.AppComponent;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public interface BaseActivity {
    App getApp();
    View getRootView();
    AppComponent getApplicationComponent();
    Analytics analytics();
}
