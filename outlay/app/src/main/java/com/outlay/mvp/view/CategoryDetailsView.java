package com.outlay.mvp.view;

import com.outlay.domain.model.Category;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface CategoryDetailsView extends MvpView {
    void showCategory(Category category);
    void finish();
}
