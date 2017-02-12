package app.outlay.di.module;

import com.google.firebase.auth.FirebaseAuth;
import app.outlay.domain.repository.AuthService;
import app.outlay.firebase.FirebaseAuthRxWrapper;
import app.outlay.firebase.FirebaseAuthService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bmelnychuk on 2/8/17.
 */
@Module
public class FirebaseModule {
    @Provides
    @Singleton
    public AuthService provideOutlayAuth(
            FirebaseAuthRxWrapper firebaseRxWrapper
    ) {
        return new FirebaseAuthService(firebaseRxWrapper);
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth(
    ) {
        return FirebaseAuth.getInstance();
    }
}
