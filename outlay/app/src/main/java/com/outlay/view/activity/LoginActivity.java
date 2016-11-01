package com.outlay.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.outlay.App;
import com.outlay.R;
import com.outlay.domain.model.User;
import com.outlay.mvp.presenter.AuthPresenter;
import com.outlay.mvp.view.AuthView;
import com.outlay.view.LoginForm;
import com.outlay.view.Navigator;

import javax.inject.Inject;

import butterknife.Bind;

public class LoginActivity extends BaseActivity implements AuthView {
    @Inject
    AuthPresenter presenter;

    @Bind(R.id.loginForm)
    LoginForm loginForm;

    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getComponent(this).inject(this);
        presenter.attachView(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginForm.setOnSignUpClickListener((email, password, src) -> {
            action = "SIGNUP";
            presenter.signUp(email, password);
        });
        loginForm.setOnSignInClickListener((email, password, src) -> {
            action = "SIGNIN";
            presenter.signIn(email, password);
        });
        loginForm.setOnPasswordForgetClick(() -> presenter.resetPassword("melnychuk.bogdan@gmail.com"));
        loginForm.setOnSkipButtonClick(v -> presenter.signInGuest());

        presenter.onCreate();
    }


    @Override
    public void setProgress(boolean running) {
        loginForm.setProgress(running);
    }

    @Override
    public void error(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void info(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(User user) {
        ((App) getApplicationContext()).createUserComponent(user);
        Navigator.goToMainScreen(this, user == null ? null : action);
        finish();
    }
}
