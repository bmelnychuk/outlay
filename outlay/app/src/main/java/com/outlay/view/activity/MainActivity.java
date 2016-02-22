package com.outlay.view.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.outlay.R;
import com.outlay.view.fragment.MainFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        setTitle(null);

        Fragment f = Fragment.instantiate(this, MainFragment.class.getName());
        getFragmentManager().beginTransaction().replace(R.id.fragment, f, MainFragment.class.getName()).commit();
    }

    @Override
    public boolean hasDrawer() {
        return false;
    }
}
