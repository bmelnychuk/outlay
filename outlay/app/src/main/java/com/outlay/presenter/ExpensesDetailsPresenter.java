package com.outlay.presenter;

import com.outlay.api.OutlayDatabaseApi;
import com.outlay.dao.Expense;
import com.outlay.view.fragment.ExpensesDetailsFragment;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ExpensesDetailsPresenter {
    private OutlayDatabaseApi api;
    private ExpensesDetailsFragment view;

    @Inject
    public ExpensesDetailsPresenter(OutlayDatabaseApi outlayDatabaseApi) {
        this.api = outlayDatabaseApi;
    }

    public void attachView(ExpensesDetailsFragment fragment) {
        this.view = fragment;
    }

    public void loadExpense(Long expenseId) {
        view.displayExpense(api.getExpenseById(expenseId));
    }

    public void loadCategories() {
        api.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> view.setCategories(response));
    }

    public void updateExpense(Expense expense) {
        DateTime dateTime = new DateTime(expense.getReportedAt().getTime());
        dateTime = dateTime.withTime(LocalTime.now());
        expense.setReportedAt(dateTime.toDate());
        if (expense.getId() == null) {
            api.insertExpense(expense);
        } else {
            api.updateExpense(expense);
        }
    }

    public void deleteExpense(Expense expense) {
        api.deleteExpense(expense);
    }
}
