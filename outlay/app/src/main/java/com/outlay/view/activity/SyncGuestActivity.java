package com.outlay.view.activity;

import android.os.Bundle;

import com.outlay.R;
import com.outlay.view.activity.base.ParentActivity;
import com.outlay.view.fragment.SyncGuestFragment;

public class SyncGuestActivity extends ParentActivity {
    private SyncGuestFragment syncGuestFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        this.initializeActivity(savedInstanceState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        syncGuestFragment = new SyncGuestFragment();
        syncGuestFragment.setArguments(getIntent().getExtras());
        addFragment(R.id.fragment, syncGuestFragment);
    }
}
