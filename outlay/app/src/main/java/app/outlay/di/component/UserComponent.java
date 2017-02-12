package app.outlay.di.component;

import app.outlay.di.module.UserModule;
import app.outlay.di.scope.UserScope;
import app.outlay.view.activity.MainActivity;
import app.outlay.view.fragment.AnalysisFragment;
import app.outlay.view.fragment.CategoriesFragment;
import app.outlay.view.fragment.CategoryDetailsFragment;
import app.outlay.view.fragment.ExpensesDetailsFragment;
import app.outlay.view.fragment.ExpensesListFragment;
import app.outlay.view.fragment.MainFragment;
import app.outlay.view.fragment.ReportFragment;

import dagger.Subcomponent;

/**
 * Created by bmelnychuk on 10/27/16.
 */

@UserScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {
    void inject(CategoriesFragment fragment);

    void inject(CategoryDetailsFragment fragment);

    void inject(ReportFragment fragment);

    void inject(ExpensesListFragment fragment);

    void inject(ExpensesDetailsFragment fragment);

    void inject(MainActivity mainActivity);

    void inject(MainFragment mainFragment2);

    void inject(AnalysisFragment analysisFragment);
}
