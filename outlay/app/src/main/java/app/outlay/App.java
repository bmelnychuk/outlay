package app.outlay;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import app.outlay.core.logger.LoggerFactory;
import app.outlay.di.component.AppComponent;
import app.outlay.di.component.DaggerAppComponent;
import app.outlay.di.component.UserComponent;
import app.outlay.di.module.AppModule;
import app.outlay.di.module.FirebaseModule;
import app.outlay.di.module.UserModule;
import app.outlay.domain.model.User;
import app.outlay.firebase.dto.adapter.UserAdapter;
import app.outlay.impl.AndroidLogger;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class App extends Application {
    private AppComponent appComponent;
    private UserComponent userComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        LoggerFactory.registerLogger(new AndroidLogger());

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public UserComponent createUserComponent(User user) {
        userComponent = appComponent.plus(new UserModule(user));
        return userComponent;
    }

    public void releaseUserComponent() {
        userComponent = null;
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .firebaseModule(new FirebaseModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public UserComponent getUserComponent() {
        return getUserComponent(true);
    }

    public UserComponent getUserComponent(boolean tryRecreate) {
        if (userComponent == null && tryRecreate) {
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            if (fbUser != null) {
                User user = UserAdapter.fromFirebaseUser(fbUser);
                createUserComponent(user);
            }
        }
        return userComponent;
    }
}
