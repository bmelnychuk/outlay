package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.domain.model.Expense;
import com.outlay.domain.repository.ExpenseRepository;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class SaveExpenseUseCase extends UseCase<Expense, Expense> {
    private ExpenseRepository expenseRepository;

    @Inject
    public SaveExpenseUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
    }

    @Override
    protected Observable<Expense> buildUseCaseObservable(Expense expense) {
        DateTime dateTime = new DateTime(expense.getReportedAt().getTime());
        dateTime = dateTime.withTime(LocalTime.now());
        expense.setReportedAt(dateTime.toDate());
        return expenseRepository.saveExpense(expense);
    }
}
