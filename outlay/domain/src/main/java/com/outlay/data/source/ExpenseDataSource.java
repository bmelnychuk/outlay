package com.outlay.data.source;

import com.outlay.domain.model.Expense;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface ExpenseDataSource {
    Observable<List<Expense>> getAll();
    Observable<List<Expense>> saveAll(List<Expense> expenses);
    Observable<Expense> saveExpense(Expense expense);
    Observable<List<Expense>> getExpenses(Date startDate, Date endDate);
    Observable<List<Expense>> getExpenses(Date startDate, Date endDate, String categoryId);
    Observable<List<Expense>> getExpenses(String categoryId);
    Observable<Expense> getById(String expenseId);
    Observable<Expense> remove(Expense expense);
    Observable<Void> removeByCategory(String categoryId);
    Observable<Void> clear();
}
