package com.outlay.view.activity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.outlay.R;
import com.outlay.view.Navigator;
import com.outlay.view.activity.base.DrawerActivity;
import com.outlay.view.fragment.MainFragment;

public class MainActivity extends DrawerActivity {

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        Bundle b = getIntent().getExtras();
        mainFragment = new MainFragment();
        mainFragment.setArguments(b);
        addFragment(R.id.fragment, mainFragment);
    }

    @Override
    protected void signOut() {
        getApp().releaseUserComponent();
        FirebaseAuth.getInstance().signOut();
        Navigator.goToLoginScreen(this);
        finish();
    }

    @Override
    protected void createUser() {
        Navigator.goToSyncGuestActivity(this);
    }
}
