package com.outlay.presenter;

import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.DeleteCategoryUseCase;
import com.outlay.domain.interactor.GetCategoryUseCase;
import com.outlay.domain.interactor.UpdateCategoryUseCase;
import com.outlay.domain.model.Category;
import com.outlay.view.fragment.CategoryDetailsFragment;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoryDetailsPresenter {
    private CategoryDetailsFragment view;
    private UpdateCategoryUseCase updateCategoryUseCase;
    private DeleteCategoryUseCase deleteCategoryUseCase;
    private GetCategoryUseCase getCategoryUseCase;

    @Inject
    public CategoryDetailsPresenter(
            UpdateCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase,
            GetCategoryUseCase getCategoryUseCase
    ) {
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
    }

    public void attachView(CategoryDetailsFragment fragment) {
        this.view = fragment;
    }

    public void loadCategory(Long id) {
        getCategoryUseCase.execute(id, new DefaultSubscriber<Category>(){
            @Override
            public void onNext(Category category) {
                view.displayCategory(category);
            }
        });

    }

    public void updateCategory(Category category) {
        updateCategoryUseCase.execute(category, new DefaultSubscriber());

    }

    public void deleteCategory(Category category) {
        deleteCategoryUseCase.execute(category, new DefaultSubscriber());
    }
}
