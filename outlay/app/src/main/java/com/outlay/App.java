package com.outlay;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.outlay.core.logger.LoggerFactory;
import com.outlay.di.component.AppComponent;
import com.outlay.di.component.DaggerAppComponent;
import com.outlay.di.component.UserComponent;
import com.outlay.di.module.AppModule;
import com.outlay.di.module.DaoModule;
import com.outlay.di.module.NetworkModule;
import com.outlay.di.module.UserModule;
import com.outlay.domain.model.OutlaySession;
import com.outlay.domain.model.User;
import com.outlay.impl.AndroidLogger;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class App extends Application {
    private AppComponent appComponent;
    private UserComponent userComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        initializeInjector();
        LoggerFactory.registerLogger(new AndroidLogger());

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public UserComponent createUserComponent(User user) {
        if (userComponent == null) {
            userComponent = appComponent.plus(new UserModule(user));
            OutlaySession.setCurrentUser(user);
        }
        return userComponent;
    }

    public void releaseUserComponent() {
        userComponent = null;
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .daoModule(new DaoModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public UserComponent getUserComponent() {
        return userComponent;
    }
}
