package com.outlay.mvp.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.outlay.core.data.AppPreferences;
import com.outlay.mvp.view.MainView;

import javax.inject.Inject;

/**
 * Created by bmelnychuk on 11/3/16.
 */

public class MainViewPresenter extends MvpPresenter<MainView> {
    private AppPreferences appPreferences;

    @Inject
    public MainViewPresenter(
            AppPreferences appPreferences
    ) {
        this.appPreferences = appPreferences;
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        appPreferences.setFirstRun(true);
        getView().onSignOut();
    }

    public void signIn() {
        appPreferences.setFirstRun(true);
        getView().onSignIn();
    }
}
