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

    @Inject
    public ExpenseRepositoryImpl(
            ExpenseDataSource expenseDataSource
    ) {
        this.expenseDataSource = expenseDataSource;
    }

    @Override
    public Observable<Expense> saveExpense(Expense expense) {
        return expenseDataSource.saveExpense(expense);
    }

    @Override
    public Observable<Expense> remove(Expense expense) {
        return expenseDataSource.remove(expense);
    }

    @Override
    public Observable<Expense> getById(Long expenseId) {
        return expenseDataSource.getById(expenseId);
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
            Long categoryId
    ) {
        return expenseDataSource.getExpenses(startDate, endDate, categoryId);
    }
}
