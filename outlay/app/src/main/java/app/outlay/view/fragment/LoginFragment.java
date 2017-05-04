package app.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.outlay.domain.model.User;
import app.outlay.mvp.presenter.LoginViewPresenter;
import app.outlay.mvp.view.LoginView;
import app.outlay.view.LoginForm;
import app.outlay.view.Navigator;
import app.outlay.view.fragment.base.BaseMvpFragment;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public class LoginFragment extends BaseMvpFragment<LoginView, LoginViewPresenter> implements LoginView {
    @Inject
    LoginViewPresenter presenter;

    @Bind(app.outlay.R.id.loginForm)
    LoginForm loginForm;

    @Bind(app.outlay.R.id.progressLayout)
    View progressLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public LoginViewPresenter createPresenter() {
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(app.outlay.R.layout.fragment_login, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginForm.setOnSignUpClickListener((email, password, src) -> {
            analytics().trackSignUp();
            presenter.signUp(email, password);
        });
        loginForm.setOnSignInClickListener((email, password, src) -> {
            analytics().trackEmailSignIn();
            presenter.signIn(email, password);
        });
        loginForm.setOnPasswordForgetClick(() -> presenter.resetPassword("melnychuk.bogdan@gmail.com"));
        loginForm.setOnSkipButtonClick(v -> {
            analytics().trackGuestSignIn();
            presenter.signInGuest();
        });

        presenter.trySignIn();
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
        getApp().createUserComponent(user);
        Navigator.goToMainScreen(getActivity());
        getActivity().finish();
    }
}
