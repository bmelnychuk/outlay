package app.outlay.view.fragment.base;

import android.support.v7.widget.Toolbar;

import app.outlay.App;
import app.outlay.analytics.Analytics;
import app.outlay.di.component.AppComponent;
import app.outlay.view.activity.base.BaseActivity;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public interface BaseFragment {
    BaseActivity getBaseActivity();
    App getApp();
    AppComponent getAppComponent();
    void setToolbar(Toolbar toolbar);
    Analytics analytics();
}
