package com.outlay.database.source;

import com.outlay.data.source.ExpenseDataSource;
import com.outlay.database.adapter.CategoryDatabaseMapper;
import com.outlay.database.adapter.ExpenseDatabaseMapper;
import com.outlay.database.dao.ExpenseDao;
import com.outlay.domain.model.Expense;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.dao.query.WhereCondition;
import rx.Observable;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public class ExpenseDatabaseSource implements ExpenseDataSource {
    private ExpenseDao expenseDao;
    private ExpenseDatabaseMapper mapper;
    private CategoryDatabaseMapper categoryDatabaseMapper;

    @Inject
    public ExpenseDatabaseSource(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
        this.categoryDatabaseMapper = new CategoryDatabaseMapper();
        this.mapper = new ExpenseDatabaseMapper(categoryDatabaseMapper);
    }

    @Override
    public Observable<List<Expense>> getAll() {
        return Observable.create(subscriber -> {
            try {
                List<com.outlay.database.dao.Expense> dbExpenses = expenseDao.loadAll();
                List<Expense> expenses = mapper.toExpenses(dbExpenses);
                subscriber.onNext(expenses);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<List<Expense>> saveAll(List<Expense> expenses) {
        return Observable.create(subscriber -> {
            try {
                List<com.outlay.database.dao.Expense> dbExpenses = mapper.fromExpenses(expenses);
                expenseDao.insertOrReplaceInTx(dbExpenses);
                subscriber.onNext(mapper.toExpenses(dbExpenses));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Expense> saveExpense(Expense expense) {
        return Observable.create(subscriber -> {
            try {
                com.outlay.database.dao.Expense dbExpense = mapper.fromExpense(expense);
                expenseDao.insertOrReplace(dbExpense);

                subscriber.onNext(mapper.toExpense(dbExpense));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate, String categoryId) {
        return Observable.create(subscriber -> {
            try {
                List<com.outlay.database.dao.Expense> dbExpenses = loadExpenses(
                        startDate,
                        endDate,
                        categoryId
                );
                List<Expense> expenses = mapper.toExpenses(dbExpenses);
                subscriber.onNext(expenses);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<List<Expense>> getExpenses(String categoryId) {
        return getExpenses(null, null, categoryId);
    }

    @Override
    public Observable<Expense> getById(String expenseId) {
        return Observable.create(subscriber -> {
            try {
                com.outlay.database.dao.Expense e = expenseDao.load(Long.valueOf(expenseId));
                Expense category = mapper.toExpense(e);
                subscriber.onNext(category);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Expense> remove(Expense expense) {
        return Observable.create(subscriber -> {
            try {
                com.outlay.database.dao.Expense daoExpense = mapper.fromExpense(expense);
                expenseDao.delete(daoExpense);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Void> clear() {
        return Observable.create(subscriber -> {
            try {
                expenseDao.deleteAll();
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

    }

    @Override
    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate) {
        return getExpenses(startDate, endDate, null);
    }

    private List<com.outlay.database.dao.Expense> loadExpenses(
            Date startDate,
            Date endDate,
            String categoryId
    ) {
        List<WhereCondition> whereConditions = new ArrayList<>();
        if (categoryId != null) {
            whereConditions.add(ExpenseDao.Properties.CategoryId.eq(Long.valueOf(categoryId)));
        }
        if (startDate != null && endDate != null) {
            whereConditions.add(ExpenseDao.Properties.ReportedAt.ge(startDate));
            whereConditions.add(ExpenseDao.Properties.ReportedAt.le(endDate));
        }

        WhereCondition whereCondition = whereConditions.get(0);

        WhereCondition[] whereConditionsArray = null;
        if (whereConditions.size() > 1) {
            whereConditionsArray = whereConditions
                    .subList(1, whereConditions.size())
                    .toArray(new WhereCondition[whereConditions.size() - 1]);
        }

        List<com.outlay.database.dao.Expense> expenses = expenseDao.queryBuilder().where(
                whereCondition,
                whereConditionsArray != null ? whereConditionsArray : new WhereCondition[0]
        ).list();
        return expenses;
    }
}
