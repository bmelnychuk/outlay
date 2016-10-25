package com.outlay.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.outlay.R;
import com.outlay.view.activity.SingleFragmentActivity;
import com.outlay.view.fragment.CategoryDetailsFragment;
import com.outlay.view.fragment.ExpensesDetailsFragment;
import com.outlay.view.fragment.ExpensesListFragment;
import com.outlay.view.fragment.ReportFragment;

import java.util.Date;

/**
 * Created by Bogdan Melnychuk on 1/24/16.
 */
public final class Page {
    public static void goToCategoryDetails(Activity activityFrom, Long categoryId) {
        Bundle b = new Bundle();
        if (categoryId != null) {
            b.putLong(CategoryDetailsFragment.ARG_CATEGORY_PARAM, categoryId);
        }
        changeFragment(activityFrom, CategoryDetailsFragment.class, b);
    }

    public static void goToReport(Activity activityFrom, Date date) {
        Bundle b = new Bundle();
        b.putLong(ReportFragment.ARG_DATE, date.getTime());
        SingleFragmentActivity.start(activityFrom, ReportFragment.class, b);
    }

    public static void goToExpensesList(Activity activityFrom, Date dateFrom, Date dateTo, Long categoryId) {
        Bundle b = new Bundle();
        if(categoryId != null) {
            b.putLong(ExpensesListFragment.ARG_CATEGORY_ID, categoryId);
        }
        if (dateFrom != null) {
            b.putLong(ExpensesListFragment.ARG_DATE_FROM, dateFrom.getTime());
        }
        if (dateTo != null) {
            b.putLong(ExpensesListFragment.ARG_DATE_TO, dateTo.getTime());
        }
        changeFragment(activityFrom, ExpensesListFragment.class, b);
    }

    public static void goToExpenseDetails(Activity activityFrom, Long expenseId) {
        Bundle b = new Bundle();
        if(expenseId != null) {
            b.putLong(ExpensesDetailsFragment.ARG_EXPENSE_ID, expenseId);
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
