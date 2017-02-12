package app.outlay.domain.repository;

import app.outlay.domain.model.Credentials;
import app.outlay.domain.model.User;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public interface AuthService {
    Observable<User> signIn(Credentials credentials);
    Observable<User> signUp(Credentials credentials);
    Observable<User> linkCredentials(Credentials credentials);
    Observable<User> signInAnonymously();
    Observable<Void> resetPassword(User user);
}
