package app.outlay.domain.interactor;

import app.outlay.core.executor.PostExecutionThread;
import app.outlay.core.executor.ThreadExecutor;
import app.outlay.domain.model.Expense;
import app.outlay.domain.repository.ExpenseRepository;

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
        DateTime dateTime = new DateTime(expense.getReportedWhen().getTime());
        dateTime = dateTime.withTime(LocalTime.now());
        expense.setReportedWhen(dateTime.toDate());
        return expenseRepository.saveExpense(expense);
    }
}
