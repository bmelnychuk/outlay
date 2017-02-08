package com.outlay.view.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.outlay.App;
import com.outlay.di.component.AppComponent;

import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class ParentActivity extends AppCompatActivity implements BaseActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
        setTitle(null);
    }

    @Override
    public View getRootView() {
        return findViewById(android.R.id.content);
    }

    @Override
    public App getApp() {
        return (App) getApplication();
    }

    @Override
    public AppComponent getApplicationComponent() {
        return getApp().getAppComponent();
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }
}
