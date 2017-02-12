package app.outlay.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import app.outlay.domain.model.Expense;
import app.outlay.view.activity.LoginActivity;
import app.outlay.view.activity.MainActivity;
import app.outlay.view.activity.SingleFragmentActivity;
import app.outlay.view.activity.SyncGuestActivity;
import app.outlay.view.fragment.AnalysisFragment;
import app.outlay.view.fragment.CategoriesFragment;
import app.outlay.view.fragment.CategoryDetailsFragment;
import app.outlay.view.fragment.ExpensesDetailsFragment;
import app.outlay.view.fragment.ExpensesListFragment;
import app.outlay.view.fragment.ReportFragment;

import java.util.Date;

/**
 * Created by Bogdan Melnychuk on 1/24/16.
 */
public final class Navigator {
    public static void goToCategoryDetails(FragmentActivity activityFrom, String categoryId) {
        Bundle b = new Bundle();
        if (categoryId != null) {
            b.putString(CategoryDetailsFragment.ARG_CATEGORY_PARAM, categoryId);
        }
        changeFragment(activityFrom, CategoryDetailsFragment.class, b);
    }

    public static void goToCategoriesList(FragmentActivity from) {
        SingleFragmentActivity.start(from, CategoriesFragment.class);
    }

    public static void goToMainScreen(FragmentActivity activityFrom) {
        Intent intent = new Intent(activityFrom, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activityFrom.startActivity(intent);
    }

    public static void goToLoginScreen(FragmentActivity activityFrom) {
        Intent intent = new Intent(activityFrom, LoginActivity.class);
        activityFrom.startActivity(intent);
    }

    public static void goToSyncGuestActivity(FragmentActivity activityFrom) {
        Intent intent = new Intent(activityFrom, SyncGuestActivity.class);
        activityFrom.startActivity(intent);
    }

    public static void goToReport(FragmentActivity activityFrom, Date date) {
        Bundle b = new Bundle();
        b.putLong(ReportFragment.ARG_DATE, date.getTime());
        SingleFragmentActivity.start(activityFrom, ReportFragment.class, b);
    }

    public static void goToExpensesList(FragmentActivity activityFrom, Date dateFrom, Date dateTo, String categoryId) {
        Bundle b = new Bundle();
        if (categoryId != null) {
            b.putString(ExpensesListFragment.ARG_CATEGORY_ID, categoryId);
        }
        if (dateFrom != null) {
            b.putLong(ExpensesListFragment.ARG_DATE_FROM, dateFrom.getTime());
        }
        if (dateTo != null) {
            b.putLong(ExpensesListFragment.ARG_DATE_TO, dateTo.getTime());
        }
        changeFragment(activityFrom, ExpensesListFragment.class, b);
    }

    public static void goToExpenseDetails(FragmentActivity activityFrom, Expense expense) {
        Bundle b = new Bundle();
        if (expense != null) {
            b.putString(ExpensesDetailsFragment.ARG_EXPENSE_ID, expense.getId());
            b.putLong(ExpensesDetailsFragment.ARG_DATE, expense.getReportedWhen().getTime());
        }
        changeFragment(activityFrom, ExpensesDetailsFragment.class, b);
    }

    public static void goToAnalysis(FragmentActivity activityFrom) {
        Bundle b = new Bundle();
        changeFragment(activityFrom, AnalysisFragment.class, b);
    }

    private static void changeFragment(FragmentActivity activityFrom, Class<?> clazz, Bundle b) {
        String className = clazz.getName();
        Fragment f = Fragment.instantiate(activityFrom, className);
        f.setArguments(b);
        activityFrom.getSupportFragmentManager()
                .beginTransaction()
                .replace(app.outlay.R.id.fragment, f, className)
                .addToBackStack(className)
                .commit();
    }
}
