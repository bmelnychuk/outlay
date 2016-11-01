package com.outlay.di.module;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.outlay.App;
import com.outlay.R;
import com.outlay.core.data.AppPreferences;
import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.domain.model.Category;
import com.outlay.executor.JobExecutor;
import com.outlay.executor.UIThread;
import com.outlay.impl.AndroidPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

/**
 * Created by Bogdan Melnychuk on 12/17/15.
 */
@Module
public class AppModule {
    private final App context;

    public AppModule(App mApplication) {
        this.context = mApplication;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return context;
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
        return new AndroidPreferencesManager(context);
    }


    @Provides
    @Singleton
    List<Category> defaultCategories() {
        List<Category> result = new ArrayList<>();
        result.add(category(context.getString(R.string.category_car), "ic_cars", ContextCompat.getColor(context, R.color.blue), 0));
        result.add(category(context.getString(R.string.category_house), "ic_house", ContextCompat.getColor(context, R.color.red), 1));
        result.add(category(context.getString(R.string.category_grocery), "ic_shopping", ContextCompat.getColor(context, R.color.green), 2));
        result.add(category(context.getString(R.string.category_games), "ic_controller", ContextCompat.getColor(context, R.color.purple), 3));
        result.add(category(context.getString(R.string.category_clothes), "ic_t_shirt", ContextCompat.getColor(context, R.color.teal), 4));
        result.add(category(context.getString(R.string.category_tickets), "ic_tag", ContextCompat.getColor(context, R.color.amber), 5));
        result.add(category(context.getString(R.string.category_sport), "ic_weightlifting", ContextCompat.getColor(context, R.color.brown), 6));
        result.add(category(context.getString(R.string.category_travel), "ic_flight", ContextCompat.getColor(context, R.color.cyan), 7));
        return result;
    }

    private static Category category(String title, String icon, int color, int order) {
        Category c = new Category();
        c.setTitle(title);
        c.setIcon(icon);
        c.setColor(color);
        c.setOrder(order);
        return c;
    }
}
