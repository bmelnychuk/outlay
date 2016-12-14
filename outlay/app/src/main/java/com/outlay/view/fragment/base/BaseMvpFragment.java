package com.outlay.view.fragment.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.outlay.App;
import com.outlay.di.component.AppComponent;
import com.outlay.mvp.presenter.MvpPresenter;
import com.outlay.mvp.view.MvpView;
import com.outlay.view.activity.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public abstract class BaseMvpFragment<VIEW extends MvpView, T extends MvpPresenter<VIEW>> extends Fragment implements BaseFragment {

    private T presenter;

    public abstract T getPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    public void setDisplayHomeAsUpEnabled(boolean value) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(value);
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

    public void setTitle(String value) {
        getActivity().setTitle(value);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = getPresenter();
        presenter.attachView((VIEW) this);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
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

    public void error(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
