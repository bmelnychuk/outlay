package com.outlay.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.outlay.R;
import com.outlay.api.OutlayDatabaseApi;
import com.outlay.domain.model.Category;
import com.outlay.domain.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class CategoryRepositoryImpl implements CategoryRepository {
    private OutlayDatabaseApi outlayDatabaseApi;
    private Context context;

    @Inject
    public CategoryRepositoryImpl(
            OutlayDatabaseApi outlayDatabaseApi,
            Context context
    ) {
        this.outlayDatabaseApi = outlayDatabaseApi;
        this.context = context;
    }

    private static Category category(String title, String icon, int color, int order) {
        Category c = new Category();
        c.setTitle(title);
        c.setIcon(icon);
        c.setColor(color);
        c.setOrder(order);
        return c;
    }

    @Override
    public Observable<List<Category>> getDefault() {
        return Observable.create(subscriber -> {
            List<Category> result = new ArrayList<>();
            result.add(category(context.getString(R.string.category_car), "ic_cars", ContextCompat.getColor(context, R.color.blue), 0));
            result.add(category(context.getString(R.string.category_house), "ic_house", ContextCompat.getColor(context, R.color.red), 1));
            result.add(category(context.getString(R.string.category_grocery), "ic_shopping", ContextCompat.getColor(context, R.color.green), 2));
            result.add(category(context.getString(R.string.category_games), "ic_controller", ContextCompat.getColor(context, R.color.purple), 3));
            result.add(category(context.getString(R.string.category_clothes), "ic_t_shirt", ContextCompat.getColor(context, R.color.teal), 4));
            result.add(category(context.getString(R.string.category_tickets), "ic_tag", ContextCompat.getColor(context, R.color.amber), 5));
            result.add(category(context.getString(R.string.category_sport), "ic_weightlifting", ContextCompat.getColor(context, R.color.brown), 6));
            result.add(category(context.getString(R.string.category_travel), "ic_flight", ContextCompat.getColor(context, R.color.cyan), 7));
            subscriber.onNext(result);
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<List<Category>> getAll() {
        return outlayDatabaseApi.getCategories()
                .map(categories -> {
                    List<Category> result = new ArrayList<>();
                    for (com.outlay.dao.Category c : categories) {
                        Category category = new Category();
                        category.setColor(c.getColor());
                        category.setIcon(c.getIcon());
                        category.setOrder(c.getOrder());
                        category.setId(c.getId());
                        category.setTitle(c.getTitle());
                        result.add(category);
                    }
                    return result;
                });
    }

    @Override
    public Observable<Category> getById(Long id) {
        return Observable.create(subscriber -> {
            com.outlay.dao.Category c = outlayDatabaseApi.getCategoryById(id);
            Category category = new Category();
            category.setColor(c.getColor());
            category.setIcon(c.getIcon());
            category.setOrder(c.getOrder());
            category.setId(c.getId());
            category.setTitle(c.getTitle());

            subscriber.onNext(category);
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<List<Category>> saveAll(List<Category> categories) {
        List<com.outlay.dao.Category> toInsert = new ArrayList<>();
        for (Category c : categories) {
            com.outlay.dao.Category category = new com.outlay.dao.Category();
            category.setColor(c.getColor());
            category.setIcon(c.getIcon());
            category.setOrder(c.getOrder());
            category.setId(c.getId());
            category.setTitle(c.getTitle());
            toInsert.add(category);
        }

        return outlayDatabaseApi.insertCategories(toInsert)
                .map(storedCategories -> {
                    List<Category> result = new ArrayList<>();
                    for (com.outlay.dao.Category c : storedCategories) {
                        Category category = new Category();
                        category.setColor(c.getColor());
                        category.setIcon(c.getIcon());
                        category.setOrder(c.getOrder());
                        category.setId(c.getId());
                        category.setTitle(c.getTitle());
                        result.add(category);
                    }
                    return result;
                });
    }

    @Override
    public Observable<Category> save(Category category) {
        return Observable.create(subscriber -> {
            com.outlay.dao.Category daoCategory = CategoryAdapter.fromCategory(category);
            outlayDatabaseApi.insertCategory(daoCategory);
            subscriber.onNext(CategoryAdapter.toCategory(daoCategory));
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Category> remove(Category category) {
        return Observable.create(subscriber -> {
            com.outlay.dao.Category daoCategory = CategoryAdapter.fromCategory(category);
            outlayDatabaseApi.deleteCategory(daoCategory);
            subscriber.onNext(CategoryAdapter.toCategory(daoCategory));
            subscriber.onCompleted();
        });
    }
}
