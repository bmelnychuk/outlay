package com.outlay.di.module;

import android.app.Application;
import android.content.Context;

import com.outlay.App;
import com.outlay.core.data.AppPreferences;
import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.executor.JobExecutor;
import com.outlay.executor.UIThread;
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

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    AppPreferences provideAppPreferences() {
        return new PreferencesManager(mApplication);
    }
}
