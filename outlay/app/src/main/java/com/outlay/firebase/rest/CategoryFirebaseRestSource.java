package com.outlay.firebase.rest;

import com.outlay.firebase.dto.adapter.CategoryAdapter;
import com.outlay.data.source.CategoryDataSource;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 11/1/16.
 */

public class CategoryFirebaseRestSource implements CategoryDataSource {
    private Firebase.Api firebaseApi;
    private User currentUser;
    private CategoryAdapter adapter;

    @Inject
    public CategoryFirebaseRestSource(
            Firebase.Api firebaseApi,
            User currentUser
    ) {
        this.firebaseApi = firebaseApi;
        this.currentUser = currentUser;
        this.adapter = new CategoryAdapter();
    }

    @Override
    public Observable<List<Category>> getAll() {
        throw new UnsupportedOperationException();
//        return firebaseApi.getCategoies(currentUser.getId(), currentUser.getToken())
//                .map(categoriesMap -> adapter.toCategories(new ArrayList<>(categoriesMap.values())));
    }

    @Override
    public Observable<Category> getById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<Category>> updateOrder(List<Category> categories) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Category> save(Category category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Category> remove(Category category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Void> clear() {
        throw new UnsupportedOperationException();
    }
}
