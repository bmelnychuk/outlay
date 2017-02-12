package app.outlay.data.source;

import app.outlay.domain.model.Expense;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface ExpenseDataSource {
    Observable<Expense> saveExpense(Expense expense);
    Observable<List<Expense>> getExpenses(Date startDate, Date endDate);
    Observable<List<Expense>> getExpenses(Date startDate, Date endDate, String categoryId);
    Observable<Expense> findExpense(String expenseId, Date date);
    Observable<Expense> remove(Expense expense);
}
