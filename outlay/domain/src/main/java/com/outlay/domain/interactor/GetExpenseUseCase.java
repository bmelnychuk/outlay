package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.domain.model.Expense;
import com.outlay.domain.repository.ExpenseRepository;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class GetExpenseUseCase extends UseCase<GetExpenseUseCase.Input, Expense> {
    private ExpenseRepository expenseRepository;

    @Inject
    public GetExpenseUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
    }

    @Override
    protected Observable<Expense> buildUseCaseObservable(GetExpenseUseCase.Input input) {
        return expenseRepository.findExpense(input.id, input.date);
    }

    public static class Input {
        private final String id;
        private final Date date;

        public Input(String id, Date date) {
            this.id = id;
            this.date = date;
        }
    }

}
