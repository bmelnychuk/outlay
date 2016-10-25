package com.outlay.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import com.outlay.App;
import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.ResetPasswordUseCase;
import com.outlay.domain.interactor.UserSignInUseCase;
import com.outlay.domain.interactor.UserSignUpUseCase;
import com.outlay.domain.model.Credentials;
import com.outlay.domain.model.User;
import com.outlay.mvp.view.AuthView;
import com.outlay.view.Navigator;

import javax.inject.Inject;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class AuthPresenter extends MvpPresenter<AuthView> {
    private UserSignInUseCase userSignInUseCase;
    private UserSignUpUseCase userSignUpUseCase;
    private ResetPasswordUseCase resetPasswordUseCase;

    @Inject
    public AuthPresenter(
            UserSignInUseCase userSignInUseCase,
            UserSignUpUseCase userSignUpUseCase,
            ResetPasswordUseCase resetPasswordUseCase
    ) {
        this.userSignInUseCase = userSignInUseCase;
        this.userSignUpUseCase = userSignUpUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    public void signIn(String email, String password, Activity context) {
        Credentials credentials = new Credentials(email, password);
        getView().setProgress(true);
        userSignInUseCase.execute(credentials, new DefaultSubscriber<User>() {
            @Override
            public void onNext(User user) {
                ((App) context.getApplicationContext()).createUserComponent(user);
                getView().setProgress(false);
                Navigator.goToMainScreen(context);
                context.finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().setProgress(false);
                getView().error(e.getLocalizedMessage());

            }
        });
    }

    public void signUp(String email, String password, Activity context) {
        Credentials credentials = new Credentials(email, password);
        getView().setProgress(true);
        userSignUpUseCase.execute(credentials, new DefaultSubscriber<User>() {
            @Override
            public void onNext(User user) {
                ((App) context.getApplicationContext()).createUserComponent(user);
                getView().setProgress(false);
                Navigator.goToMainScreen(context);
                context.finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().setProgress(false);
                getView().error(e.getLocalizedMessage());
            }
        });
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
                getView().error("Problem while resetting your password");
            }
        });
    }
}
