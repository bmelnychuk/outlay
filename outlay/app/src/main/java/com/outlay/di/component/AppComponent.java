package com.outlay.di.component;

import android.content.Context;

import com.outlay.di.module.AppModule;
import com.outlay.di.module.DaoModule;
import com.outlay.di.module.UserModule;
import com.outlay.view.activity.LoginActivity;
import com.outlay.view.activity.MainActivity;
import com.outlay.view.fragment.CategoriesFragment;
import com.outlay.view.fragment.CategoryDetailsFragment;
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
    UserComponent plus(UserModule userModule);

    void inject(LoginActivity loginActivity);

    Context getApplication();


}
