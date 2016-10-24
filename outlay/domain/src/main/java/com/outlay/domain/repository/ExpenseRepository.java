package com.outlay.domain.repository;

import com.outlay.domain.model.Expense;
import com.outlay.domain.model.Report;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public interface ExpenseRepository {
    Observable<Expense> saveExpense(Expense expense);
    Observable<List<Expense>> getExpenses(Date startDate, Date endDate);
}
