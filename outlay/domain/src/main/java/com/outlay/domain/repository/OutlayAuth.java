package com.outlay.domain.repository;

import com.outlay.domain.model.Credentials;
import com.outlay.domain.model.User;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public interface OutlayAuth {
    Observable<User> signIn(Credentials credentials);
    Observable<User> signUp(Credentials credentials);
    Observable<User> signInAnonymously();
    Observable<Void> resetPassword(User user);
}
