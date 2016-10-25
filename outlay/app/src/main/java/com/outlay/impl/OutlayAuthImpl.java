package com.outlay.impl;

import com.outlay.domain.model.Credentials;
import com.outlay.domain.model.User;
import com.outlay.domain.repository.OutlayAuth;
import com.outlay.firebase.FirebaseRxWrapper;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class OutlayAuthImpl implements OutlayAuth {
    private FirebaseRxWrapper firebaseWrapper;

    @Inject
    public OutlayAuthImpl(FirebaseRxWrapper firebaseWrapper) {
        this.firebaseWrapper = firebaseWrapper;
    }

    @Override
    public Observable<User> signIn(Credentials credentials) {
        return firebaseWrapper.signIn(credentials.getEmail(), credentials.getPassword())
                .map(authResult -> {
                    User result = new User();
                    result.setEmail(authResult.getUser().getEmail());
                    result.setId(authResult.getUser().getUid());
                    return result;
                });
    }

    @Override
    public Observable<User> signUp(Credentials credentials) {
        return firebaseWrapper.signUp(credentials.getEmail(), credentials.getPassword())
                .map(authResult -> {
                    User result = new User();
                    result.setEmail(authResult.getUser().getEmail());
                    result.setId(authResult.getUser().getUid());
                    return result;
                });
    }

    @Override
    public Observable<Void> resetPassword(User user) {
        return firebaseWrapper.resetPassword(user);
    }
}
