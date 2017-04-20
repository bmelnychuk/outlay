package app.outlay.mvp.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby.mvp.*;

import app.outlay.core.executor.DefaultSubscriber;
import app.outlay.domain.interactor.LinkAccountUseCase;
import app.outlay.domain.interactor.ResetPasswordUseCase;
import app.outlay.domain.interactor.UserSignInUseCase;
import app.outlay.domain.interactor.UserSignUpUseCase;
import app.outlay.domain.model.Credentials;
import app.outlay.domain.model.User;
import app.outlay.mvp.view.LoginView;

import javax.inject.Inject;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class LoginViewPresenter extends MvpBasePresenter<LoginView> {
    private UserSignInUseCase userSignInUseCase;
    private UserSignUpUseCase userSignUpUseCase;
    private ResetPasswordUseCase resetPasswordUseCase;
    private LinkAccountUseCase linkAccountUseCase;

    @Inject
    public LoginViewPresenter(
            UserSignInUseCase userSignInUseCase,
            UserSignUpUseCase userSignUpUseCase,
            ResetPasswordUseCase resetPasswordUseCase,
            LinkAccountUseCase linkAccountUseCase
    ) {
        this.userSignInUseCase = userSignInUseCase;
        this.userSignUpUseCase = userSignUpUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.linkAccountUseCase = linkAccountUseCase;
    }

    public void signIn(String email, String password) {
        Credentials credentials = new Credentials(email, password);
        signIn(credentials);
    }

    public void signInGuest() {
        signIn(Credentials.GUEST);
    }

    private void signIn(Credentials credentials) {
        if (getView()!=null){
            getView().setProgress(true);
        }

        userSignInUseCase.execute(credentials, new DefaultSubscriber<User>() {
            @Override
            public void onNext(User user) {
                onAuthSuccess(user);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().setProgress(false);
                getView().error(e);
            }
        });
    }

    public void signUp(String email, String password) {
        if (getView()!=null){
            getView().setProgress(true);
        }

        Credentials credentials = new Credentials(email, password);

        userSignUpUseCase.execute(credentials, new DefaultSubscriber<User>() {
            @Override
            public void onNext(User user) {
                onAuthSuccess(user);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().setProgress(false);
                getView().error(e);
            }
        });
    }

    public void linkAccount(String email, String password) {
        linkAccountUseCase.execute(new Credentials(email, password), new DefaultSubscriber<User>() {
            @Override
            public void onNext(User user) {
                if (getView()!=null){
                    getView().onSuccess(user);
                }
            }
        });
    }

    public void trySignIn() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            User user = new User();
            user.setId(firebaseUser.getUid());
            user.setEmail(firebaseUser.getEmail());
            user.setAnonymous(firebaseUser.isAnonymous());
            user.setUserName(firebaseUser.getDisplayName());
            if (getView()!=null){
                getView().onSuccess(user);
            }
        }
    }

    private void onAuthSuccess(User user) {
        if (getView()!=null){
            getView().onSuccess(user);
        }
    }

    public void resetPassword(String email) {
        User user = new User();
        user.setEmail(email);
        resetPasswordUseCase.execute(user, new DefaultSubscriber() {
            @Override
            public void onCompleted() {
                if (getView()!=null){
                    getView().info("Email with was sent");
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (getView()!=null){
                    getView().error(new Exception("Problem while resetting your password"));
                }
            }
        });
    }
}
