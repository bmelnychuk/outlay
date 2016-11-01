package com.outlay.impl;

import com.outlay.data.source.CategoryDataSource;
import com.outlay.domain.model.Category;
import com.outlay.domain.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class CategoryRepositoryImpl implements CategoryRepository {
    private CategoryDataSource databaseSource;
    private CategoryDataSource firebaseSource;

    private Map<String, Category> categoryMap;

    private CategoryDataSource getDataSource() {
        return firebaseSource == null ? databaseSource : firebaseSource;
    }

    @Inject
    public CategoryRepositoryImpl(
            CategoryDataSource databaseSource,
            CategoryDataSource firebaseSource
    ) {
        this.databaseSource = databaseSource;
        this.firebaseSource = firebaseSource;
    }

    @Override
    public Observable<List<Category>> getAll() {
        if (getCategoryMap() != null && !getCategoryMap().isEmpty()) {
            return Observable.just(new ArrayList<>(getCategoryMap().values()));
        }
        return getDataSource().getAll().doOnNext(categories -> {
            for (Category c : categories) {
                getCategoryMap().put(c.getId(), c);
            }
        });
    }

    @Override
    public Observable<Category> getById(String id) {
        if (getCategoryMap() != null && !getCategoryMap().isEmpty()) {
            return Observable.just(getCategoryMap().get(id));
        } else {
            return getDataSource().getById(id);
        }
    }

    @Override
    public Observable<List<Category>> updateAll(List<Category> categories) {
        this.categoryMap = null;
        return getDataSource().updateAll(categories);
    }

    @Override
    public Observable<Category> save(Category category) {
        this.categoryMap = null;
        return getDataSource().save(category);
    }

    @Override
    public Observable<Category> remove(Category category) {
        this.categoryMap = null;
        return getDataSource().remove(category);
    }

    @Override
    public void clearCache() {
        this.categoryMap = null;
    }

    private Map<String, Category> getCategoryMap() {
        if (this.categoryMap == null) {
            categoryMap = new LinkedHashMap<>();
        }
        return categoryMap;
    }


}
