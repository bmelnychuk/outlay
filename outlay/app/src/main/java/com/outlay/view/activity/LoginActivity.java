package com.outlay.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.outlay.App;
import com.outlay.R;
import com.outlay.mvp.presenter.AuthPresenter;
import com.outlay.mvp.view.AuthView;
import com.outlay.view.LoginForm;
import com.outlay.view.Navigator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements AuthView {
    @Inject
    AuthPresenter presenter;

    @Bind(R.id.loginForm)
    LoginForm loginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getComponent(this).inject(this);
        presenter.attachView(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginForm.setOnSignUpClickListener((email, password, src) -> presenter.signUp(email, password, this));
        loginForm.setOnSignInClickListener((email, password, src) -> {
            presenter.signIn(email, password, this);
        });
        loginForm.setOnPasswordForgetClick(() -> presenter.resetPassword("melnychuk.bogdan@gmail.com"));
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
}
