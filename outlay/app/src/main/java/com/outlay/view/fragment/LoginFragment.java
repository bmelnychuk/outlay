package com.outlay.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.outlay.R;
import com.outlay.domain.model.User;
import com.outlay.mvp.presenter.AuthPresenter;
import com.outlay.mvp.view.AuthView;
import com.outlay.view.LoginForm;
import com.outlay.view.Navigator;
import com.outlay.view.fragment.base.BaseMvpFragment;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public class LoginFragment extends BaseMvpFragment<AuthView, AuthPresenter> implements AuthView {
    public static final String ARG_ACTION = "_argAction";

    @Inject
    AuthPresenter presenter;

    @Bind(R.id.loginForm)
    LoginForm loginForm;

    @Bind(R.id.progressLayout)
    View progressLayout;

    private String action;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getAppComponent().inject(this);
    }

    @Override
    public AuthPresenter getPresenter() {
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        action = "default";
        if (getArguments() != null) {
            action = getArguments().getString(ARG_ACTION, "default");
        }
        switch (action) {
            case "sync":
                loginForm.setToggleModeButtonVisible(false);
                loginForm.setMode(LoginForm.MODE_SIGN_UP);
                loginForm.setOnSignUpClickListener((email, password, src) -> {
                    presenter.signUp(email, password, true);
                });
                break;
            default:
                loginForm.setOnSignUpClickListener((email, password, src) -> {
                    presenter.signUp(email, password, false);
                });
                loginForm.setOnSignInClickListener((email, password, src) -> {
                    presenter.signIn(email, password);
                });
                loginForm.setOnPasswordForgetClick(() -> presenter.resetPassword("melnychuk.bogdan@gmail.com"));
                loginForm.setOnSkipButtonClick(v -> presenter.signInGuest());

                presenter.onCreate();
        }


    }

    @Override
    public void setProgress(boolean running) {
        progressLayout.setVisibility(running ? View.VISIBLE : View.GONE);
    }

    @Override
    public void info(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(User user) {
        switch (action) {
            case "sync":
                getApp().createUserComponent(user);
                Navigator.goToMainScreen(getActivity());
                getActivity().finish();
                break;
            default:
                getApp().createUserComponent(user);
                Navigator.goToMainScreen(getActivity());
                getActivity().finish();
        }

    }
}
