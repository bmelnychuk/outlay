package com.outlay.mvp.presenter;

import com.outlay.core.executor.DefaultSubscriber;
import com.outlay.domain.interactor.GetCategoriesUseCase;
import com.outlay.domain.interactor.UpdateCategoriesUseCase;
import com.outlay.domain.model.Category;
import com.outlay.mvp.view.CategoriesView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoriesPresenter extends MvpPresenter<CategoriesView> {
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

    public void loadCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                getView().showCategories(categories);
            }
        });
    }

    public void refreshOrder(List<Category> categories) {
        updateCategoriesUseCase.execute(categories, new DefaultSubscriber());
    }
}
