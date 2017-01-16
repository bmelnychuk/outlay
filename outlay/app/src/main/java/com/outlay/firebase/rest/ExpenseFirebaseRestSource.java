package com.outlay.firebase.rest;

import com.outlay.data.source.ExpenseDataSource;
import com.outlay.domain.model.Expense;
import com.outlay.domain.model.User;
import com.outlay.firebase.dto.adapter.ExpenseAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 11/1/16.
 */

public class ExpenseFirebaseRestSource implements ExpenseDataSource {
    private Firebase.Api firebaseApi;
    private User currentUser;
    private ExpenseAdapter adapter;

    public ExpenseFirebaseRestSource(Firebase.Api firebaseApi, User currentUser) {
        this.firebaseApi = firebaseApi;
        this.currentUser = currentUser;
        this.adapter = new ExpenseAdapter();
    }

    @Override
    public Observable<Expense> saveExpense(Expense expense) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<Expense>> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<Expense>> saveAll(List<Expense> expenses) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate) {
        return getExpenses(startDate, endDate, null);
    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate, String categoryId) {
        return firebaseApi.getExpenses(currentUser.getId(), currentUser.getToken())
                .map(categoriesMap -> adapter.toExpenses(new ArrayList<>(categoriesMap.values())));
    }

    @Override
    public Observable<List<Expense>> getExpenses(String categoryId) {
        return getExpenses(null, null, categoryId);
    }

    @Override
    public Observable<Expense> getById(String expenseId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Expense> remove(Expense expense) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Void> removeByCategory(String categoryId) {
        return null;
    }

    @Override
    public Observable<Void> clear() {
        throw new UnsupportedOperationException();
    }
}
