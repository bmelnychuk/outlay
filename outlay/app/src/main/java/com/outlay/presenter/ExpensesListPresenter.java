package com.outlay.presenter;

import com.outlay.api.OutlayDatabaseApi;
import com.outlay.dao.Category;
import com.outlay.view.fragment.ExpensesListFragment;

import java.util.Date;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ExpensesListPresenter {
    private OutlayDatabaseApi api;
    private ExpensesListFragment view;

    @Inject
    public ExpensesListPresenter(OutlayDatabaseApi outlayDatabaseApi) {
        this.api = outlayDatabaseApi;
    }

    public void attachView(ExpensesListFragment fragment) {
        this.view = fragment;
    }

    public void loadExpenses(Date dateFrom, Date dateTo, Long categoryId) {
        api.getExpenses(dateFrom, dateTo, categoryId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> view.displayExpenses(response));
    }

    public Category getCategoryById(Long id) {
        return api.getCategoryById(id);
    }

}
