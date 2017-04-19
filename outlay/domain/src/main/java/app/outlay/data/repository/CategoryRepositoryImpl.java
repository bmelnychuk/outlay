package app.outlay.data.repository;

import app.outlay.core.utils.TextUtils;
import app.outlay.data.source.CategoryDataSource;
import app.outlay.domain.model.Category;
import app.outlay.domain.repository.CategoryRepository;

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
            CategoryDataSource firebaseSource
    ) {
        this.firebaseSource = firebaseSource;
    }

    private CategoryDataSource getDataSource() {
        return firebaseSource;
    }

    @Override
    public Observable<List<Category>> getAll() {
        if (categoryMap != null) {
            return Observable.just(new ArrayList<>(categoryMap.values()));
        }
        return getDataSource().getAll().doOnNext(this::cacheCategories);
    }

    @Override
    public Observable<Category> getById(String id) {
        if (categoryMap != null && categoryMap.containsKey(id)) {
            return Observable.just(categoryMap.get(id));
        } else {
            return getDataSource().getById(id);
        }
    }

    @Override
    public Observable<List<Category>> updateOrder(List<Category> categories) {
        clearCache();
        return getDataSource().updateOrder(categories).doOnNext(this::cacheCategories);
    }

    @Override
    public Observable<Category> save(Category category) {
        return getAll().switchMap(categories -> {
            if (TextUtils.isEmpty(category.getId())) {
                if (categories.isEmpty()) {
                    category.setOrder(0);
                } else {
                    category.setOrder(categories.get(categories.size() - 1).getOrder() + 1);
                }
            }
            return getDataSource().save(category);
        }).doOnNext(this::cacheCategory);
    }

    @Override
    public Observable<Category> remove(Category category) {
        clearCache();
        return getDataSource().remove(category);
    }

    private void clearCache() {
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
