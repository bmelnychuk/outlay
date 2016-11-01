package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.data.sync.CategoryDataSync;
import com.outlay.data.sync.ExpenseDataSync;
import com.outlay.domain.repository.CategoryRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class InitUseCase extends UseCase<Void, Void> {
    private CategoryDataSync categoryDataSync;
    private ExpenseDataSync expenseDataSync;
    private CategoryRepository categoryRepository;

    @Inject
    public InitUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryDataSync categoryDataSync,
            ExpenseDataSync expenseDataSync,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryDataSync = categoryDataSync;
        this.expenseDataSync = expenseDataSync;
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(Void aVoid) {
        return Observable.zip(categoryDataSync.sync(), expenseDataSync.sync(), (categories, expenses) -> {
            categoryRepository.clearCache();
            return null;
        });
    }
}
