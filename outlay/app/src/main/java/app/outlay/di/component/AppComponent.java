package app.outlay.di.component;

import android.content.Context;

import app.outlay.analytics.Analytics;
import app.outlay.di.module.AppModule;
import app.outlay.di.module.FirebaseModule;
import app.outlay.di.module.UserModule;
import app.outlay.view.activity.base.ParentActivity;
import app.outlay.view.activity.LoginActivity;
import app.outlay.view.fragment.LoginFragment;
import app.outlay.view.fragment.SyncGuestFragment;

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
