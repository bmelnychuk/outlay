package app.outlay.firebase;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import app.outlay.core.utils.DateUtils;
import app.outlay.data.source.CategoryDataSource;
import app.outlay.data.source.ExpenseDataSource;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;
import app.outlay.domain.model.User;
import app.outlay.firebase.dto.ExpenseDto;
import app.outlay.firebase.dto.adapter.ExpenseAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
    public Observable<Expense> saveExpense(Expense expense) {
        return Observable.create(subscriber -> {
            String key = expense.getId();
            if (TextUtils.isEmpty(key)) {
                key = mDatabase.child("users")
                        .child(currentUser.getId())
                        .child("expenses")
                        .child(DateUtils.toYearMonthString(expense.getReportedWhen()))
                        .push().getKey();
                expense.setId(key);
            }

            ExpenseDto expenseDto = adapter.fromExpense(expense);

            DatabaseReference databaseReference = mDatabase.child("users")
                    .child(currentUser.getId())
                    .child("expenses")
                    .child(DateUtils.toYearMonthString(expense.getReportedWhen()))
                    .child(key);
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
            DatabaseReference databaseReference = mDatabase.child("users").child(currentUser.getId()).child("expenses");
            Query query = databaseReference.orderByKey();
            query = query.startAt(DateUtils.toYearMonthString(startDate)).endAt(DateUtils.toYearMonthString(endDate));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Expense> expenses = new ArrayList<>();
                    for (DataSnapshot monthSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot expensesSnapshot : monthSnapshot.getChildren()) {
                            ExpenseDto expenseDto = expensesSnapshot.getValue(ExpenseDto.class);
                            if (!TextUtils.isEmpty(categoryId) && !expenseDto.getCategoryId().equals(categoryId)) {
                                continue;
                            }
                            expenses.add(adapter.toExpense(expenseDto));
                        }
                    }
                    // at this point we ave monthly data

                    Iterator<Expense> expenseIterator = expenses.iterator();
                    while (expenseIterator.hasNext()) {
                        Expense e = expenseIterator.next();
                        if (!DateUtils.isInPeriod(e.getReportedWhen(), startDate, endDate)) {
                            expenseIterator.remove();
                        }
                    }

                    subscriber.onNext(expenses);
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
                }).switchMap(categoryMap ->
                        listObservable.flatMap(Observable::from)
                                .map(expense -> {
                                    String currentCatId = expense.getCategory().getId();
                                    return expense.setCategory(categoryMap.get(currentCatId));
                                })
                                .filter(expense -> expense.getCategory() != null)
                                .toSortedList((e1, e2) -> (int) (e1.getReportedWhen().getTime() - e2.getReportedWhen().getTime()))
                );
    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate) {
        return getExpenses(startDate, endDate, null);
    }

    @Override
    public Observable<Expense> findExpense(String expenseId, Date date) {
        Observable<Expense> expenseObservable = Observable.create(subscriber -> {
            mDatabase.child("users")
                    .child(currentUser.getId())
                    .child("expenses")
                    .child(DateUtils.toYearMonthString(date))
                    .child(expenseId)
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
                        .map(category ->
                                category == null ? null : expense.setCategory(category)
                        )
                );
    }

    @Override
    public Observable<Expense> remove(Expense expense) {
        return Observable.create(subscriber -> {

            DatabaseReference expenseRef = mDatabase.child("users")
                    .child(currentUser.getId())
                    .child("expenses")
                    .child(DateUtils.toYearMonthString(expense.getReportedWhen()))
                    .child(expense.getId());
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
}