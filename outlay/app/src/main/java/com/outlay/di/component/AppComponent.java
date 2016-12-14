package com.outlay.di.component;

import android.content.Context;

import com.outlay.di.module.AppModule;
import com.outlay.di.module.DaoModule;
import com.outlay.di.module.NetworkModule;
import com.outlay.di.module.UserModule;
import com.outlay.view.activity.base.StaticContentActivity;
import com.outlay.view.activity.LoginActivity;
import com.outlay.view.fragment.LoginFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Bogdan Melnychuk on 12/17/15.
 */
@Singleton
@Component(modules = {AppModule.class, DaoModule.class, NetworkModule.class})
public interface AppComponent {
    UserComponent plus(UserModule userModule);
    Context getApplication();

    void inject(LoginActivity loginActivity);
    void inject(StaticContentActivity staticContentActivity);
    void inject(LoginFragment loginFragment);
}
