package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.data.sync.DataSync;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Expense;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class InitUseCase extends UseCase<Void, Void> {
    private DataSync<Category> categoryDataSync;
    private DataSync<Expense> expenseDataSync;

    @Inject
    public InitUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            DataSync<Category> categoryDataSync,
            DataSync<Expense> expenseDataSync
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryDataSync = categoryDataSync;
        this.expenseDataSync = expenseDataSync;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(Void aVoid) {
        return Observable.zip(categoryDataSync.synchronize(), expenseDataSync.synchronize(), (categories, expenses) -> null);
    }
}
