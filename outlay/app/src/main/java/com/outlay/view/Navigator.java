package com.outlay.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.outlay.R;
import com.outlay.domain.model.Expense;
import com.outlay.view.activity.LoginActivity;
import com.outlay.view.activity.MainActivity;
import com.outlay.view.activity.SingleFragmentActivity;
import com.outlay.view.fragment.CategoryDetailsFragment;
import com.outlay.view.fragment.ExpensesDetailsFragment;
import com.outlay.view.fragment.ExpensesListFragment;
import com.outlay.view.fragment.LoginFragment;
import com.outlay.view.fragment.MainFragment;
import com.outlay.view.fragment.ReportFragment;

import java.util.Date;

/**
 * Created by Bogdan Melnychuk on 1/24/16.
 */
public final class Navigator {
    public static void goToCategoryDetails(Activity activityFrom, String categoryId) {
        Bundle b = new Bundle();
        if (categoryId != null) {
            b.putString(CategoryDetailsFragment.ARG_CATEGORY_PARAM, categoryId);
        }
        changeFragment(activityFrom, CategoryDetailsFragment.class, b);
    }

    public static void goToMainScreen(Activity activityFrom) {
        Intent intent = new Intent(activityFrom, MainActivity.class);
        activityFrom.startActivity(intent);
    }

    public static void goToLoginScreen(Activity activityFrom, String action) {
        Bundle b = new Bundle();
        b.putString(LoginFragment.ARG_ACTION, action);
        Intent intent = new Intent(activityFrom, LoginActivity.class);
        intent.putExtras(b);
        activityFrom.startActivity(intent);
        activityFrom.finish();
    }

    public static void goToMainScreen(Activity activityFrom, String action) {
        Bundle b = new Bundle();
        b.putString(MainFragment.ACTION, action);
        Intent intent = new Intent(activityFrom, MainActivity.class);
        intent.putExtras(b);
        activityFrom.startActivity(intent);
    }

    public static void goToReport(Activity activityFrom, Date date) {
        Bundle b = new Bundle();
        b.putLong(ReportFragment.ARG_DATE, date.getTime());
        SingleFragmentActivity.start(activityFrom, ReportFragment.class, b);
    }

    public static void goToExpensesList(Activity activityFrom, Date dateFrom, Date dateTo, String categoryId) {
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

    public static void goToExpenseDetails(Activity activityFrom, Expense expense) {
        Bundle b = new Bundle();
        if (expense != null) {
            b.putString(ExpensesDetailsFragment.ARG_EXPENSE_ID, expense.getId());
            b.putLong(ExpensesDetailsFragment.ARG_DATE, expense.getReportedWhen().getTime());
        }
        changeFragment(activityFrom, ExpensesDetailsFragment.class, b);
    }

    private static void changeFragment(Activity activityFrom, Class<?> clazz, Bundle b) {
        String className = clazz.getName();
        Fragment f = Fragment.instantiate(activityFrom, className);
        f.setArguments(b);
        activityFrom.getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, f, className)
                .addToBackStack(className)
                .commit();
    }
}
