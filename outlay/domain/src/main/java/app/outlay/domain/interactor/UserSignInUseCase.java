package app.outlay.domain.interactor;

import app.outlay.core.executor.PostExecutionThread;
import app.outlay.core.executor.ThreadExecutor;
import app.outlay.domain.model.Credentials;
import app.outlay.domain.model.User;
import app.outlay.domain.repository.AuthService;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class UserSignInUseCase extends UseCase<Credentials, User> {
    private AuthService authService;

    @Inject
    public UserSignInUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AuthService authService
    ) {
        super(threadExecutor, postExecutionThread);
        this.authService = authService;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(Credentials credentials) {
        if (credentials.isGuestCredentials()) {
            return authService.signInAnonymously();
        } else {
            return authService.signIn(credentials);
        }
    }
}
