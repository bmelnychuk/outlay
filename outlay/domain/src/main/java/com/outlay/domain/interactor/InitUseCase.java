package com.outlay.domain.interactor;

import com.outlay.core.data.AppPreferences;
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

public class InitUseCase extends UseCase<Void, List<Category>> {
    private CategoryRepository categoryRepository;
    private AppPreferences appPreferences;

    @Inject
    public InitUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryRepository categoryRepository,
            AppPreferences appPreferences
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
        this.appPreferences = appPreferences;
    }

    @Override
    protected Observable<List<Category>> buildUseCaseObservable(Void aVoid) {
        if (appPreferences.isFirstRun()) {
            return categoryRepository.getDefault()
                    .switchMap(categories -> {
                        appPreferences.setFirstRun(false);
                        return categoryRepository.updateAll(categories);
                    });
        } else {
            return categoryRepository.getAll();
        }
    }
}
