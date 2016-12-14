package com.outlay.view.fragment.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.outlay.App;
import com.outlay.di.component.AppComponent;
import com.outlay.view.activity.base.BaseActivity;
import com.outlay.view.activity.base.StaticContentActivity;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class StaticContentFragment extends Fragment implements BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDisplayHomeAsUpEnabled(boolean value) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(value);
    }

    public void setTitle(String value) {
        getActivity().setTitle(value);
    }

    public View getRootView() {
        return ((StaticContentActivity)getActivity()).getRootView();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public App getApp() {
        return getBaseActivity().getApp();
    }

    @Override
    public AppComponent getAppComponent() {
        return getBaseActivity().getApplicationComponent();
    }
}
