package com.outlay.di.module;

import android.app.Application;

import com.outlay.App;
import com.outlay.preferences.PreferencesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Bogdan Melnychuk on 12/17/15.
 */
@Module
public class AppModule {
    private final App mApplication;

    public AppModule(App mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideAppContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    PreferencesManager providePreferencesManager() {
        return new PreferencesManager(mApplication);
    }
}
