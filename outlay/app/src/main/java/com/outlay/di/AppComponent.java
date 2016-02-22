package com.outlay.di;

import android.app.Application;

import com.outlay.di.module.AppModule;
import com.outlay.di.module.DaoModule;
import com.outlay.view.activity.MainActivity;
import com.outlay.view.fragment.CategoryDetailsFragment;
import com.outlay.view.fragment.CategoriesFragment;
import com.outlay.view.fragment.ExpensesDetailsFragment;
import com.outlay.view.fragment.ExpensesListFragment;
import com.outlay.view.fragment.MainFragment;
import com.outlay.view.fragment.ReportFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Bogdan Melnychuk on 12/17/15.
 */
@Singleton
@Component(modules = {AppModule.class, DaoModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(CategoriesFragment fragment);
    void inject(CategoryDetailsFragment fragment);
    void inject(MainFragment fragment);
    void inject(ReportFragment fragment);
    void inject(ExpensesListFragment fragment);
    void inject(ExpensesDetailsFragment fragment);

    Application getApplication();
}
