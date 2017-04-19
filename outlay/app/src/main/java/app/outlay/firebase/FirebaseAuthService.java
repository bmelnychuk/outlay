package app.outlay.firebase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import app.outlay.domain.model.Credentials;
import app.outlay.domain.model.User;
import app.outlay.domain.repository.AuthService;
import app.outlay.firebase.dto.adapter.UserAdapter;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class FirebaseAuthService implements AuthService {
    private FirebaseAuthRxWrapper firebaseWrapper;

    @Inject
    public FirebaseAuthService(FirebaseAuthRxWrapper firebaseWrapper) {
        this.firebaseWrapper = firebaseWrapper;
    }

    @Override
    public Observable<User> signIn(Credentials credentials) {
        return firebaseWrapper.signIn(credentials.getEmail(), credentials.getPassword())
                .map(AuthResult::getUser)
                .map(UserAdapter::fromFirebaseUser);
    }

    @Override
    public Observable<User> signUp(Credentials credentials) {
        return firebaseWrapper.signUp(credentials.getEmail(), credentials.getPassword())
                .map(AuthResult::getUser)
                .map(UserAdapter::fromFirebaseUser);
    }

    @Override
    public Observable<User> linkCredentials(Credentials credentials) {
        AuthCredential emailCredentials = EmailAuthProvider.getCredential(credentials.getEmail(), credentials.getPassword());
        return firebaseWrapper.linkAccount(emailCredentials)
                .map(AuthResult::getUser)
                .map(UserAdapter::fromFirebaseUser);
    }

    @Override
    public Observable<User> signInAnonymously() {
        return firebaseWrapper.signInAnonymously()
                .map(AuthResult::getUser)
                .map(UserAdapter::fromFirebaseUser);
    }

    @Override
    public Observable<Void> resetPassword(User user) {
        return firebaseWrapper.resetPassword(user);
    }
}
