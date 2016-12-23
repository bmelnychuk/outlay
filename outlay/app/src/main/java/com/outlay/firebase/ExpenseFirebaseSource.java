package com.outlay.firebase;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.outlay.data.source.CategoryDataSource;
import com.outlay.data.source.ExpenseDataSource;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Expense;
import com.outlay.domain.model.User;
import com.outlay.firebase.dto.ExpenseDto;
import com.outlay.firebase.dto.adapter.ExpenseAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/27/16.
 */

public class ExpenseFirebaseSource implements ExpenseDataSource {
    private DatabaseReference mDatabase;
    private CategoryDataSource categoryDataSource;
    private ExpenseAdapter adapter;
    private User currentUser;

    @Inject
    public ExpenseFirebaseSource(
            User currentUser,
            DatabaseReference databaseReference,
            CategoryDataSource categoryDataSource
    ) {
        this.currentUser = currentUser;
        this.categoryDataSource = categoryDataSource;
        mDatabase = databaseReference;
        adapter = new ExpenseAdapter();
    }

    @Override
    public Observable<List<Expense>> getAll() {
        return Observable.create(subscriber -> {
            DatabaseReference expenses = mDatabase.child("users").child(currentUser.getId()).child("expenses");

            expenses.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Expense> categories = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ExpenseDto expenseDto = postSnapshot.getValue(ExpenseDto.class);
                        categories.add(adapter.toExpense(expenseDto));
                    }
                    subscriber.onNext(categories);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
        });
    }

    @Override
    public Observable<List<Expense>> saveAll(List<Expense> expenses) {
        return Observable.create(subscriber -> {
            List<ExpenseDto> expenseDtos = adapter.fromExpenses(expenses);
            Map<String, Object> expenseDtosMap = new HashMap<>();
            for (ExpenseDto categoryDto : expenseDtos) {
                expenseDtosMap.put(categoryDto.getId(), categoryDto);
            }

            DatabaseReference databaseReference = mDatabase.child("users").child(currentUser.getId())
                    .child("expenses");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subscriber.onNext(expenses);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
            databaseReference.updateChildren(expenseDtosMap);
        });
    }

    @Override
    public Observable<Expense> saveExpense(Expense expense) {
        return Observable.create(subscriber -> {
            String key = expense.getId();
            if (TextUtils.isEmpty(key)) {
                key = mDatabase.child("users").child(currentUser.getId()).child("expenses").push().getKey();
                expense.setId(key);
            }

            ExpenseDto expenseDto = adapter.fromExpense(expense);

            DatabaseReference databaseReference = mDatabase.child("users").child(currentUser.getId())
                    .child("expenses").child(key);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subscriber.onNext(expense);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
            databaseReference.setValue(expenseDto);
        });
    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate, String categoryId) {
        final Observable<List<Expense>> listObservable = Observable.create(subscriber -> {
            DatabaseReference expenses = mDatabase.child("users").child(currentUser.getId()).child("expenses");
            Query query = null;

            if (startDate != null && endDate != null) {
                query = expenses.orderByChild("reportedAt");
                query = query.startAt(startDate.getTime(), "reportedAt").endAt(endDate.getTime(), "reportedAt");
            }

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Expense> categories = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ExpenseDto expenseDto = postSnapshot.getValue(ExpenseDto.class);
                        if (!TextUtils.isEmpty(categoryId) && !expenseDto.getCategoryId().equals(categoryId)) {
                            continue;
                        }
                        categories.add(adapter.toExpense(expenseDto));
                    }
                    subscriber.onNext(categories);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
        });

        return categoryDataSource.getAll()
                .map(categories -> {
                    Map<String, Category> categoryMap = new HashMap<>();
                    for (Category c : categories) {
                        categoryMap.put(c.getId(), c);
                    }
                    return categoryMap;
                }).switchMap(categoryMap -> listObservable.flatMap(expenses -> Observable.from(expenses))
                        .map(expense -> {
                            String currentCatId = expense.getCategory().getId();
                            return expense.setCategory(categoryMap.get(currentCatId));
                        }).toList());
    }

    @Override
    public Observable<List<Expense>> getExpenses(String categoryId) {
        return getExpenses(null, null, categoryId);
    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate) {
        return getExpenses(startDate, endDate, null);
    }

    @Override
    public Observable<Expense> getById(String expenseId) {
        Observable<Expense> expenseObservable = Observable.create(subscriber -> {
            mDatabase.child("users").child(currentUser.getId()).child("expenses").child(expenseId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ExpenseDto expenseDto = dataSnapshot.getValue(ExpenseDto.class);
                            subscriber.onNext(adapter.toExpense(expenseDto));
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });

        return expenseObservable
                .switchMap(expense -> categoryDataSource.getById(expense.getCategory().getId())
                        .map(category -> expense.setCategory(category)));
    }

    @Override
    public Observable<Expense> remove(Expense expense) {
        return Observable.create(subscriber -> {

            DatabaseReference expenseRef = mDatabase.child("users").child(currentUser.getId())
                    .child("expenses").child(expense.getId());
            expenseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subscriber.onNext(expense);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
            expenseRef.removeValue();
        });
    }

    @Override
    public Observable<Void> clear() {
        throw new UnsupportedOperationException();
    }
}
