package com.outlay.presenter;

import com.outlay.api.OutlayDatabaseApi;
import com.outlay.dao.Category;
import com.outlay.view.fragment.CategoryDetailsFragment;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoryDetailsPresenter {
    private OutlayDatabaseApi api;
    private CategoryDetailsFragment view;

    @Inject
    public CategoryDetailsPresenter(OutlayDatabaseApi outlayDatabaseApi) {
        this.api = outlayDatabaseApi;
    }

    public void attachView(CategoryDetailsFragment fragment) {
        this.view = fragment;
    }

    public void loadCategory(Long id) {
        view.displayCategory(api.getCategoryById(id));
    }

    public void updateCategory(Category category) {
        if (category.getId() == null) {
            category.setOrder(Integer.MAX_VALUE);
            api.insertCategory(category);
        } else {
            api.updateCategory(category);
        }

    }

    public void deleteCategory(Category category) {
        api.deleteExpensesByCategory(category);
        api.deleteCategory(category);
    }
}
