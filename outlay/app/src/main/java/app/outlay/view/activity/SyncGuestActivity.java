package app.outlay.view.activity;

import android.os.Bundle;

import app.outlay.view.activity.base.ParentActivity;
import app.outlay.view.fragment.SyncGuestFragment;

public class SyncGuestActivity extends ParentActivity {
    private SyncGuestFragment syncGuestFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.outlay.R.layout.activity_single_fragment);
        this.initializeActivity(savedInstanceState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        syncGuestFragment = new SyncGuestFragment();
        syncGuestFragment.setArguments(getIntent().getExtras());
        addFragment(app.outlay.R.id.fragment, syncGuestFragment);
    }
}
