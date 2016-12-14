package com.outlay.view.fragment.base;

import android.support.v7.widget.Toolbar;

import com.outlay.App;
import com.outlay.di.component.AppComponent;
import com.outlay.view.activity.base.BaseActivity;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public interface BaseFragment {
    BaseActivity getBaseActivity();
    App getApp();
    AppComponent getAppComponent();
    void setToolbar(Toolbar toolbar);
}
