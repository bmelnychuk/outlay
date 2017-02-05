package com.outlay.mvp.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.outlay.core.data.AppPreferences;
import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.ResetPasswordUseCase;
import com.outlay.domain.interactor.UserSignInUseCase;
import com.outlay.domain.interactor.UserSignUpUseCase;
import com.outlay.domain.model.Credentials;
import com.outlay.domain.model.User;
import com.outlay.mvp.view.AuthView;

import javax.inject.Inject;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class AuthPresenter extends MvpPresenter<AuthView> {
    private UserSignInUseCase userSignInUseCase;
    private UserSignUpUseCase userSignUpUseCase;
    private ResetPasswordUseCase resetPasswordUseCase;
    private AppPreferences appPreferences;

    @Inject
    public AuthPresenter(
            UserSignInUseCase userSignInUseCase,
            UserSignUpUseCase userSignUpUseCase,
            ResetPasswordUseCase resetPasswordUseCase,
            AppPreferences appPreferences
    ) {
        this.userSignInUseCase = userSignInUseCase;
        this.userSignUpUseCase = userSignUpUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.appPreferences = appPreferences;
    }

    public void signIn(String email, String password) {
        Credentials credentials = new Credentials(email, password);
        getView().setProgress(true);
        userSignInUseCase.execute(credentials, new DefaultSubscriber<User>() {
            @Override
            public void onNext(User user) {
                onAuthSuccess(user);
                getView().setProgress(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().setProgress(false);
                getView().error(e);

            }
        });
    }

    public void signInGuest() {
        onAuthSuccess(User.ANONYMOUS);
    }

    public void signUp(String email, String password, boolean sync) {
        Credentials credentials = new Credentials(email, password);
        getView().setProgress(true);
        userSignUpUseCase.execute(new UserSignUpUseCase.Input(credentials, sync), new DefaultSubscriber<User>() {
            @Override
            public void onNext(User user) {
                onAuthSuccess(user);
                getView().setProgress(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().setProgress(false);
                getView().error(e);
            }
        });
    }

    public void onCreate() {
        if (!appPreferences.isFirstRun()) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                User user = new User();
                user.setId(firebaseUser.getUid());
                user.setEmail(firebaseUser.getEmail());
                getView().onSuccess(user);
            }
        }
    }

    private void onAuthSuccess(User user) {
        appPreferences.setFirstRun(false);
        getView().onSuccess(user);
    }

    public void resetPassword(String email) {
        User user = new User();
        user.setEmail(email);
        resetPasswordUseCase.execute(user, new DefaultSubscriber() {
            @Override
            public void onCompleted() {
                getView().info("Email with was sent");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().error(new Exception("Problem while resetting your password"));
            }
        });
    }
}
