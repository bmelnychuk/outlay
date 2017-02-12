package app.outlay.domain.repository;

import app.outlay.domain.model.Category;

import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public interface CategoryRepository {
    Observable<List<Category>> getAll();

    Observable<Category> getById(String id);

    Observable<List<Category>> updateOrder(List<Category> categories);

    Observable<Category> save(Category category);

    Observable<Category> remove(Category category);
}
