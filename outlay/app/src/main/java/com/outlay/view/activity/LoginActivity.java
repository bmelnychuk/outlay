package com.outlay.view.activity;

import android.os.Bundle;

import com.outlay.R;
import com.outlay.view.activity.base.StaticContentActivity;
import com.outlay.view.fragment.LoginFragment;

public class LoginActivity extends StaticContentActivity {
    private LoginFragment loginFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        this.initializeActivity(savedInstanceState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        loginFragment = new LoginFragment();
        addFragment(R.id.fragment, loginFragment);
    }
}
