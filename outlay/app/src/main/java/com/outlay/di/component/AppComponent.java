package com.outlay.di.component;

import android.content.Context;

import com.outlay.analytics.Analytics;
import com.outlay.di.module.AppModule;
import com.outlay.di.module.FirebaseModule;
import com.outlay.di.module.UserModule;
import com.outlay.view.activity.base.ParentActivity;
import com.outlay.view.activity.LoginActivity;
import com.outlay.view.fragment.LoginFragment;
import com.outlay.view.fragment.SyncGuestFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Bogdan Melnychuk on 12/17/15.
 */
@Singleton
@Component(modules = {AppModule.class, FirebaseModule.class})
public interface AppComponent {
    UserComponent plus(UserModule userModule);
    Context getApplication();
    Analytics analytics();

    void inject(LoginActivity loginActivity);
    void inject(ParentActivity staticContentActivity);
    void inject(LoginFragment loginFragment);
    void inject(SyncGuestFragment syncGuestFragment);
}
