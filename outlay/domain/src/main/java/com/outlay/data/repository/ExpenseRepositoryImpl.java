package com.outlay.data.repository;

import com.outlay.data.source.ExpenseDataSource;
import com.outlay.domain.model.Expense;
import com.outlay.domain.repository.ExpenseRepository;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class ExpenseRepositoryImpl implements ExpenseRepository {
    private ExpenseDataSource expenseDataSource;
    private ExpenseDataSource expenseFirebaseSource;

    @Inject
    public ExpenseRepositoryImpl(
            ExpenseDataSource expenseDataSource,
            ExpenseDataSource expenseFirebaseSource
    ) {
        this.expenseDataSource = expenseDataSource;
        this.expenseFirebaseSource = expenseFirebaseSource;
    }

    private ExpenseDataSource getDataSource() {
        return expenseFirebaseSource == null ? expenseDataSource : expenseFirebaseSource;
    }

    @Override
    public Observable<Expense> saveExpense(Expense expense) {
        return getDataSource().saveExpense(expense);
    }

    @Override
    public Observable<Expense> remove(Expense expense) {
        return getDataSource().remove(expense);
    }

    @Override
    public Observable<Expense> getById(String expenseId) {
        return getDataSource().getById(expenseId);
    }

    @Override
    public Observable<List<Expense>> getExpenses(
            Date startDate,
            Date endDate
    ) {
        return getExpenses(startDate, endDate, null);
    }

    @Override
    public Observable<List<Expense>> getExpenses(
            Date startDate,
            Date endDate,
            String categoryId
    ) {
        return getDataSource().getExpenses(startDate, endDate, categoryId);
    }
}
