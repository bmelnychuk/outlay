package app.outlay.view.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import app.outlay.App;
import app.outlay.analytics.Analytics;
import app.outlay.di.component.AppComponent;
import app.outlay.view.activity.base.BaseActivity;
import app.outlay.view.activity.base.ParentActivity;

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
    public Analytics analytics() {
        return getAppComponent().analytics();
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
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(value);
        }
    }

    public void setTitle(String value) {
        getActivity().setTitle(value);
    }

    public View getRootView() {
        return ((ParentActivity) getActivity()).getRootView();
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
