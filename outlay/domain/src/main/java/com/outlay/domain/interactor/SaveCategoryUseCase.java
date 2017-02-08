package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.domain.model.Category;
import com.outlay.domain.repository.CategoryRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class SaveCategoryUseCase extends UseCase<Category, Category> {
    private CategoryRepository categoryRepository;

    @Inject
    public SaveCategoryUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Observable<Category> buildUseCaseObservable(Category category) {
        return categoryRepository.save(category);
    }
}
