package com.outlay.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.outlay.domain.model.Credentials;
import com.outlay.domain.model.User;
import com.outlay.domain.repository.OutlayAuth;
import com.outlay.firebase.FirebaseAuthRxWrapper;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class OutlayAuthImpl implements OutlayAuth {
    private FirebaseAuthRxWrapper firebaseWrapper;

    @Inject
    public OutlayAuthImpl(FirebaseAuthRxWrapper firebaseWrapper) {
        this.firebaseWrapper = firebaseWrapper;
    }

    @Override
    public Observable<User> signIn(Credentials credentials) {
        return firebaseWrapper.signIn(credentials.getEmail(), credentials.getPassword())
                .map(authResult -> authResult.getUser())
                .map(authUser -> {
                    User result = new User();
                    result.setEmail(authUser.getEmail());
                    result.setId(authUser.getUid());
                    return result;
                });
    }

    @Override
    public Observable<User> signUp(Credentials credentials) {
        return firebaseWrapper.signUp(credentials.getEmail(), credentials.getPassword())
                .map(authResult -> authResult.getUser())
                .map(firebaseUser -> {
                    User result = new User();
                    result.setEmail(firebaseUser.getEmail());
                    result.setId(firebaseUser.getUid());
                    return result;
                });
    }

    @Override
    public Observable<User> signInAnonymously() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isAnonymous()) {
            User result = new User();
            result.setEmail(currentUser.getEmail());
            result.setId(currentUser.getUid());
            return Observable.just(result);
        } else {
            return firebaseWrapper.signInAnonymously()
                    .map(authResult -> authResult.getUser())
                    .map(firebaseUser -> {
                        User result = new User();
                        result.setEmail(firebaseUser.getEmail());
                        result.setId(firebaseUser.getUid());
                        return result;
                    });
        }
    }

    @Override
    public Observable<Void> resetPassword(User user) {
        return firebaseWrapper.resetPassword(user);
    }
}
