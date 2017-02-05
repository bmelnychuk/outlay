package com.outlay.data.sync;

import com.outlay.data.source.ExpenseDataSource;
import com.outlay.domain.model.Expense;

import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 11/1/16.
 */

public class ExpenseDataSync extends DataSync<Expense, SyncSource<Expense>> {
    public ExpenseDataSync(ExpenseDataSource localDataSource, ExpenseDataSource remoteDataSource) {
        super(
                new ExpenseDataSync.ExpenseSyncSource(localDataSource),
                new ExpenseDataSync.ExpenseSyncSource(remoteDataSource)
        );
    }

    private static class ExpenseSyncSource implements SyncSource<Expense> {
        private ExpenseDataSource expenseDataSource;

        public ExpenseSyncSource(ExpenseDataSource expenseDataSource) {
            this.expenseDataSource = expenseDataSource;
        }

        @Override
        public Observable<List<Expense>> getAll() {
//            return expenseDataSource.getAll();
            return null;
        }

        @Override
        public Observable<List<Expense>> saveAll(List<Expense> items) {
            return expenseDataSource.saveAll(items);
        }

        @Override
        public Observable<Void> clear() {
//            return expenseDataSource.clear();
            return null;
        }
    }
}
