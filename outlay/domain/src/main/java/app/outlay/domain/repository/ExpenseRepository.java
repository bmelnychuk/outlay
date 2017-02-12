package app.outlay.domain.repository;

import app.outlay.domain.model.Expense;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public interface ExpenseRepository {
    Observable<Expense> saveExpense(Expense expense);

    Observable<Expense> remove(Expense expense);

    Observable<Expense> findExpense(String expenseId, Date date);

    Observable<List<Expense>> getExpenses(Date startDate, Date endDate);

    Observable<List<Expense>> getExpenses(Date startDate, Date endDate, String categoryId);
}
