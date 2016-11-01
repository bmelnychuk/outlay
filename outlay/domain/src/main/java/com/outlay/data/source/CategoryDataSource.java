package com.outlay.data.source;

import com.outlay.domain.model.Category;

import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface CategoryDataSource {
    Observable<List<Category>> getAll();

    Observable<Category> getById(String id);

    Observable<List<Category>> updateAll(List<Category> categories);

    Observable<Category> save(Category category);

    Observable<Category> remove(Category category);
}
