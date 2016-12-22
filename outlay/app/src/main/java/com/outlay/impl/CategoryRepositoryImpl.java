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

    @Inject
    public CategoryRepositoryImpl(
            CategoryDataSource databaseSource,
            CategoryDataSource firebaseSource
    ) {
        this.databaseSource = databaseSource;
        this.firebaseSource = firebaseSource;
    }

    private CategoryDataSource getDataSource() {
        return databaseSource == null ? firebaseSource : databaseSource;
    }

    @Override
    public Observable<List<Category>> getAll() {
        if (categoryMap != null) {
            return Observable.just(new ArrayList<>(categoryMap.values()));
        }
        return getDataSource().getAll().doOnNext(categories -> cacheCategories(categories));
    }

    @Override
    public Observable<Category> getById(String id) {
        if (categoryMap != null) {
            return Observable.just(categoryMap.get(id));
        } else {
            return getDataSource().getById(id);
        }
    }

    @Override
    public Observable<List<Category>> updateAll(List<Category> categories) {
        clearCache();
        return getDataSource().updateAll(categories);
    }

    @Override
    public Observable<Category> save(Category category) {
        int order = -1;
        if (categoryMap != null) {
            order = categoryMap.size();
            clearCache();
        }

        if (order != -1) {
            category.setOrder(order);
            return getDataSource().save(category);
        } else {
            return getAll().switchMap(categories -> {
                category.setOrder(categories.size());
                return getDataSource().save(category);
            });
        }
    }

    @Override
    public Observable<Category> remove(Category category) {
        clearCache();
        return getDataSource().remove(category);
    }

    @Override
    public void clearCache() {
        this.categoryMap = null;
    }


    private void cacheCategories(List<Category> categories) {
        if (categories != null) {
            for (Category c : categories) {
                cacheCategory(c);
            }
        }
    }

    private void cacheCategory(Category category) {
        if (this.categoryMap == null) {
            categoryMap = new LinkedHashMap<>();
        }
        categoryMap.put(category.getId(), category);
    }
}
