package com.outlay.api;

import android.database.Cursor;

import com.outlay.dao.Category;
import com.outlay.dao.CategoryDao;
import com.outlay.dao.DaoSession;
import com.outlay.dao.Expense;
import com.outlay.dao.ExpenseDao;
import com.outlay.model.Summary;
import com.outlay.core.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.dao.query.DeleteQuery;
import rx.Observable;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class OutlayDatabaseApi {
    private CategoryDao categoryDao;
    private ExpenseDao expenseDao;
    private DaoSession daoSession;

    @Inject
    public OutlayDatabaseApi(CategoryDao categoryDao, ExpenseDao expenseDao, DaoSession session) {
        this.categoryDao = categoryDao;
        this.expenseDao = expenseDao;
        this.daoSession = session;
    }


    public Observable<List<Category>> getCategories() {
        return Observable.defer(() -> Observable.just(categoryDao.queryBuilder().orderAsc(CategoryDao.Properties.Order).list()));
    }

    public Category getCategoryById(Long id) {
        return categoryDao.load(id);
    }

    public void updateCategory(Category category) {
        categoryDao.update(category);
    }

    public long insertCategory(Category category) {
        return categoryDao.insertOrReplace(category);
    }

    public void deleteCategory(Category category) {
        categoryDao.delete(category);
    }

    public Expense getExpenseById(Long id) {
        return expenseDao.load(id);
    }


    public void updateExpense(Expense e) {
        expenseDao.update(e);
    }

    public void deleteExpense(Expense e) {
        expenseDao.delete(e);
    }

    public Expense insertExpense(Expense expense) {
        expenseDao.insert(expense);
        return expense;
    }

    public void deleteExpensesByCategory(Category category) {
        DeleteQuery<Expense> deleteQuery = expenseDao.queryBuilder().where(ExpenseDao.Properties.CategoryId.ge(category.getId())).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    public Observable<List<Category>> insertCategories(List<Category> categories) {
        return Observable.create(subscriber -> {
            categoryDao.insertOrReplaceInTx(categories);
            subscriber.onNext(categories);
            subscriber.onCompleted();
        });
    }

    public Observable<List<Category>> updateCategories(List<Category> categories) {
        return Observable.create(subscriber -> {
            categoryDao.updateInTx(categories);
            subscriber.onNext(categories);
            subscriber.onCompleted();
        });
    }

    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate) {
        return getExpenses(startDate, endDate, null);
    }

    public Observable<List<Expense>> getExpenses(Date startDate, Date endDate, Long categoryId) {
        if (categoryId == null) {
            return Observable.create(subscriber -> {
                List<Expense> expenses = expenseDao.queryBuilder().where(
                        ExpenseDao.Properties.ReportedAt.ge(startDate),
                        ExpenseDao.Properties.ReportedAt.le(endDate)
                ).list();
                subscriber.onNext(expenses);
                subscriber.onCompleted();
            });
        } else {
            return Observable.create(subscriber -> {
                List<Expense> expenses = expenseDao.queryBuilder().where(
                        ExpenseDao.Properties.ReportedAt.ge(startDate),
                        ExpenseDao.Properties.ReportedAt.le(endDate),
                        ExpenseDao.Properties.CategoryId.eq(categoryId)
                ).list();
                subscriber.onNext(expenses);
                subscriber.onCompleted();
            });
        }
    }

    public Observable<Summary> getSummary(Date date) {
        return Observable.create(subscriber -> {
            Summary summary = new Summary();
            summary.setMonthAmount(getSumForPeriod(DateUtils.getMonthStart(date), DateUtils.getMonthEnd(date)));
            summary.setWeekAmount(getSumForPeriod(DateUtils.getWeekStart(date), DateUtils.getWeekEnd(date)));
            summary.setDayAmount(getSumForPeriod(DateUtils.getDayStart(date), DateUtils.getDayEnd(date)));
            summary.setDate(date);
            summary.setCategories(getMostPayedCategories(DateUtils.getDayStart(date), DateUtils.getDayEnd(date)));
            subscriber.onNext(summary);
            subscriber.onCompleted();
        });
    }

    private double getSumForPeriod(Date dateFrom, Date dateTo) {
        Cursor cursor = daoSession.getDatabase().rawQuery(
                "SELECT SUM(AMOUNT) FROM EXPENSE WHERE REPORTED_AT >= ? AND REPORTED_AT <= ?",
                new String[]{String.valueOf(dateFrom.getTime()), String.valueOf(dateTo.getTime())});
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return 0;
    }

    private List<Category> getMostPayedCategories(Date dateFrom, Date dateTo) {
        List<Category> result = new ArrayList<>();
        String query = "SELECT * FROM CATEGORY INNER JOIN EXPENSE ON CATEGORY._id = EXPENSE.CATEGORY_ID " +
                "WHERE EXPENSE.REPORTED_AT >= ? AND EXPENSE.REPORTED_AT <= ? " +
                "GROUP BY CATEGORY._id ORDER BY AMOUNT DESC LIMIT 4";

        Cursor cursor = daoSession.getDatabase().rawQuery(
                query,
                new String[]{String.valueOf(dateFrom.getTime()), String.valueOf(dateTo.getTime())});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result.add(categoryDao.readEntity(cursor, 0));
            } while (cursor.moveToNext());
        }

        return result;
    }

}
