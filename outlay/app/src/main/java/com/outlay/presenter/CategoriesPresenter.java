package com.outlay.presenter;

import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.GetCategoriesUseCase;
import com.outlay.domain.interactor.UpdateCategoriesUseCase;
import com.outlay.domain.model.Category;
import com.outlay.view.fragment.CategoriesFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoriesPresenter {
    private CategoriesFragment view;
    private GetCategoriesUseCase getCategoriesUseCase;
    private UpdateCategoriesUseCase updateCategoriesUseCase;

    @Inject
    public CategoriesPresenter(
            GetCategoriesUseCase getCategoriesUseCase,
            UpdateCategoriesUseCase updateCategoriesUseCase
    ) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.updateCategoriesUseCase = updateCategoriesUseCase;
    }

    public void attachView(CategoriesFragment fragment) {
        this.view = fragment;
    }

    public void loadCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                view.displayCategories(categories);
            }
        });
    }


    public void updateCategories(List<Category> categories) {
        updateCategoriesUseCase.execute(categories, new DefaultSubscriber() {

        });
    }
}
