package com.outlay.presenter;

import com.outlay.api.OutlayDatabaseApi;
import com.outlay.dao.Category;
import com.outlay.view.fragment.CategoriesFragment;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoriesPresenter {
    private OutlayDatabaseApi api;
    private CategoriesFragment view;

    @Inject
    public CategoriesPresenter(OutlayDatabaseApi outlayDatabaseApi) {
        this.api = outlayDatabaseApi;
    }

    public void attachView(CategoriesFragment fragment) {
        this.view = fragment;
    }

    public void loadCategories() {
        api.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> view.displayCategories(response));
    }


    public void updateCategories(List<Category> categories) {
        api.updateCategories(categories)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
