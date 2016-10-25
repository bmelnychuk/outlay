package com.outlay.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.outlay.R;
import com.outlay.data.source.CategoryDataSource;
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
    private Context context;
    private CategoryDataSource databaseSource;

    @Inject
    public CategoryRepositoryImpl(
            CategoryDataSource databaseSource,
            Context context
    ) {
        this.databaseSource = databaseSource;
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
        return databaseSource.getAll();
    }

    @Override
    public Observable<Category> getById(Long id) {
        return databaseSource.getById(id);
    }

    @Override
    public Observable<List<Category>> saveAll(List<Category> categories) {
        return databaseSource.saveAll(categories);
    }

    @Override
    public Observable<Category> save(Category category) {
        return databaseSource.save(category);
    }

    @Override
    public Observable<Category> remove(Category category) {
        return databaseSource.remove(category);
    }
}
