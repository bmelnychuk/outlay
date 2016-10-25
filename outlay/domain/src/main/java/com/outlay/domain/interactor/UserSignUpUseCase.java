package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.domain.model.Credentials;
import com.outlay.domain.model.User;
import com.outlay.domain.repository.OutlayAuth;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class UserSignUpUseCase extends UseCase<Credentials, User> {
    private OutlayAuth outlayAuth;

    @Inject
    public UserSignUpUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            OutlayAuth outlayAuth
    ) {
        super(threadExecutor, postExecutionThread);
        this.outlayAuth = outlayAuth;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(Credentials credentials) {
        return outlayAuth.signUp(credentials);
    }
}
