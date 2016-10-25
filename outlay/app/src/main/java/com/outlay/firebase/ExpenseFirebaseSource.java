package com.outlay.firebase;

import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.outlay.data.source.ExpenseDataSource;
import com.outlay.domain.model.Expense;
import com.outlay.domain.model.User;
import com.outlay.firebase.dto.ExpenseDto;
import com.outlay.firebase.dto.adapter.ExpenseAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/27/16.
 */

public class ExpenseFirebaseSource implements ExpenseDataSource {
    private DatabaseReference mDatabase;
    private ExpenseAdapter adapter;
    private User currentUser;

    @Inject
    public ExpenseFirebaseSource(
            User currentUser,
            DatabaseReference databaseReference
    ) {
        this.currentUser = currentUser;
        //TODO as param?
        mDatabase = databaseReference;
        adapter = new ExpenseAdapter();
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

            Task<Void> task = mDatabase.child("users").child(currentUser.getId())
                    .child("expenses").child(key)
                    .setValue(expenseDto);
            task.addOnCompleteListener(resultTask -> {
                if (task.isSuccessful()) {
                    subscriber.onNext(expense);
                    subscriber.onCompleted();
                } else {
                    Exception e = task.getException();
                    subscriber.onError(e);
                }
            });
        });
    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate, String categoryId) {
        return Observable.create(subscriber -> {
            mDatabase.child("users").child(currentUser.getId()).child("expenses")
                    .orderByChild("reportedAt")
                    .startAt(startDate.getTime()).endAt(endDate.getTime())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
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
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate) {
        return getExpenses(startDate, endDate, null);
    }

    @Override
    public Observable<Expense> getById(String expenseId) {
        return Observable.create(subscriber -> {
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
    }

    @Override
    public Observable<Expense> remove(Expense expense) {
        return Observable.create(subscriber -> {
            Task<Void> task = mDatabase.child("users").child(currentUser.getId())
                    .child("expenses").child(expense.getId())
                    .removeValue();
            task.addOnCompleteListener(resultTask -> {
                if (task.isSuccessful()) {
                    subscriber.onCompleted();
                } else {
                    Exception e = task.getException();
                    subscriber.onError(e);
                }
            });
        });
    }
}
