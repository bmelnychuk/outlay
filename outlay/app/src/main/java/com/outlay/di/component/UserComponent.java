package com.outlay.di.component;

import com.outlay.di.module.UserModule;
import com.outlay.di.scope.UserScope;
import com.outlay.view.fragment.CategoriesFragment;
import com.outlay.view.fragment.CategoryDetailsFragment;
import com.outlay.view.fragment.ExpensesDetailsFragment;
import com.outlay.view.fragment.ExpensesListFragment;
import com.outlay.view.fragment.MainFragment;
import com.outlay.view.fragment.ReportFragment;

import dagger.Subcomponent;

/**
 * Created by bmelnychuk on 10/27/16.
 */

@UserScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {
    void inject(CategoriesFragment fragment);

    void inject(CategoryDetailsFragment fragment);

    void inject(MainFragment fragment);

    void inject(ReportFragment fragment);

    void inject(ExpensesListFragment fragment);

    void inject(ExpensesDetailsFragment fragment);
}
