package app.outlay.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import app.outlay.domain.model.User;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class FirebaseAuthRxWrapper {
    private FirebaseAuth firebaseAuth;

    @Inject
    public FirebaseAuthRxWrapper(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public Observable<String> getUserToken(FirebaseUser firebaseUser) {
        return Observable.create(subscriber -> {
            Task<GetTokenResult> task = firebaseUser.getToken(true);

            task.addOnCompleteListener(resultTask -> {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                    subscriber.onNext(token);
                    subscriber.onCompleted();
                } else {
                    Exception e = task.getException();
                    subscriber.onError(e);
                }
            });
        });
    }

    private void addListener(Task<AuthResult> task, rx.Subscriber<? super AuthResult> subscriber) {
        task.addOnCompleteListener(resultTask -> {
            if (task.isSuccessful()) {
                AuthResult authResult = task.getResult();
                subscriber.onNext(authResult);
                subscriber.onCompleted();
            } else {
                Exception e = task.getException();
                subscriber.onError(e);
            }
        });
    }

    public Observable<AuthResult> signUp(String email, String password) {
        return Observable.create(subscriber -> {
            Task<AuthResult> task = firebaseAuth.createUserWithEmailAndPassword(email, password);
            addListener(task, subscriber);
        });
    }

    public Observable<AuthResult> signIn(String email, String password) {
        return Observable.create(subscriber -> {
            Task<AuthResult> task = firebaseAuth.signInWithEmailAndPassword(email, password);
            addListener(task, subscriber);
        });
    }

    public Observable<AuthResult> signInAnonymously() {
        return Observable.create(subscriber -> {
            Task<AuthResult> task = firebaseAuth.signInAnonymously();
            addListener(task, subscriber);
        });
    }

    public Observable<AuthResult> linkAccount(AuthCredential credentials) {
        return Observable.create(subscriber -> {
            Task<AuthResult> task = firebaseAuth.getCurrentUser().linkWithCredential(credentials);
            addListener(task, subscriber);
        });
    }


    public Observable<Void> resetPassword(User user) {
        return Observable.create(subscriber -> {
            Task<Void> task = firebaseAuth.sendPasswordResetEmail(user.getEmail());
            task.addOnCompleteListener(resultTask -> {
                if (task.isSuccessful()) {
                    subscriber.onCompleted();
                } else {
                    Exception e = task.getException();
                    subscriber.onError(e);
                }
            });
        });
    }
}
