package app.outlay.domain.interactor;

import app.outlay.core.executor.PostExecutionThread;
import app.outlay.core.executor.ThreadExecutor;
import app.outlay.domain.model.Category;
import app.outlay.domain.repository.CategoryRepository;

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
