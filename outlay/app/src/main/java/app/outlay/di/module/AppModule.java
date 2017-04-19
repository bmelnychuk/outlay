package app.outlay.di.module;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import app.outlay.App;
import app.outlay.analytics.Analytics;
import app.outlay.core.data.AppPreferences;
import app.outlay.core.executor.PostExecutionThread;
import app.outlay.core.executor.ThreadExecutor;
import app.outlay.domain.model.Category;
import app.outlay.executor.JobExecutor;
import app.outlay.executor.UIThread;
import app.outlay.firebase.FirebaseAnalyticsImpl;
import app.outlay.impl.AndroidPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
    Analytics provideAnalytics() {
        return new FirebaseAnalyticsImpl(context);
    }

    @Provides
    @Singleton
    List<Category> defaultCategories() {
        List<Category> result = new ArrayList<>();
        result.add(category(context.getString(app.outlay.R.string.category_car), "ic_cars", ContextCompat.getColor(context, app.outlay.R.color.blue), 0));
        result.add(category(context.getString(app.outlay.R.string.category_house), "ic_house", ContextCompat.getColor(context, app.outlay.R.color.red), 1));
        result.add(category(context.getString(app.outlay.R.string.category_grocery), "ic_shopping", ContextCompat.getColor(context, app.outlay.R.color.green), 2));
        result.add(category(context.getString(app.outlay.R.string.category_games), "ic_controller", ContextCompat.getColor(context, app.outlay.R.color.purple), 3));
        result.add(category(context.getString(app.outlay.R.string.category_clothes), "ic_t_shirt", ContextCompat.getColor(context, app.outlay.R.color.teal), 4));
        result.add(category(context.getString(app.outlay.R.string.category_tickets), "ic_tag", ContextCompat.getColor(context, app.outlay.R.color.amber), 5));
        result.add(category(context.getString(app.outlay.R.string.category_sport), "ic_weightlifting", ContextCompat.getColor(context, app.outlay.R.color.brown), 6));
        result.add(category(context.getString(app.outlay.R.string.category_travel), "ic_flight", ContextCompat.getColor(context, app.outlay.R.color.cyan), 7));
        return result;
    }

    @Provides
    @Singleton
    Gson providerGson() {
        return new GsonBuilder().create();
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
