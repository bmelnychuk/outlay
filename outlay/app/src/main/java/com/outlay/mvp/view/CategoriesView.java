package com.outlay.mvp.view;

import com.outlay.domain.model.Category;

import java.util.List;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface CategoriesView extends MvpView {
    void showCategories(List<Category> categoryList);

}