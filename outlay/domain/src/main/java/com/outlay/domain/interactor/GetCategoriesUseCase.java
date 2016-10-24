package com.outlay.domain.interactor;

import com.outlay.core.executor.PostExecutionThread;
import com.outlay.core.executor.ThreadExecutor;
import com.outlay.domain.model.Category;
import com.outlay.domain.repository.CategoryRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class GetCategoriesUseCase extends UseCase<Void, List<Category>> {
    private CategoryRepository categoryRepository;

    @Inject
    public GetCategoriesUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Observable<List<Category>> buildUseCaseObservable(Void aVoid) {
        return categoryRepository.getAll();
    }
}
