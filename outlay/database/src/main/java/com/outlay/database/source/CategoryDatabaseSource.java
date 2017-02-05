package com.outlay.database.source;

import com.outlay.data.source.CategoryDataSource;
import com.outlay.data.sync.SyncFrom;
import com.outlay.database.adapter.CategoryDatabaseMapper;
import com.outlay.database.dao.CategoryDao;
import com.outlay.database.dao.ExpenseDao;
import com.outlay.domain.model.Category;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.dao.query.DeleteQuery;
import rx.Observable;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public class CategoryDatabaseSource implements CategoryDataSource, SyncFrom<Category> {
    private CategoryDao categoryDao;
    private ExpenseDao expenseDao;
    private CategoryDatabaseMapper categoryMapper;

    @Inject
    public CategoryDatabaseSource(
            CategoryDao categoryDao,
            ExpenseDao expenseDao
    ) {
        this.categoryDao = categoryDao;
        this.expenseDao = expenseDao;
        this.categoryMapper = new CategoryDatabaseMapper();
    }

    @Override
    public Observable<List<Category>> getAll() {
        return Observable.create(subscriber -> {
            try {
                List<com.outlay.database.dao.Category> categories = categoryDao.queryBuilder().orderAsc(CategoryDao.Properties.Order).list();
                List<Category> result = categoryMapper.toCategories(categories);
                subscriber.onNext(result);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Category> getById(String id) {
        return Observable.create(subscriber -> {
            try {
                com.outlay.database.dao.Category c = categoryDao.load(Long.valueOf(id));
                Category category = categoryMapper.toCategory(c);
                subscriber.onNext(category);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<List<Category>> updateOrder(List<Category> categories) {
        return Observable.create(subscriber -> {
            try {
                List<com.outlay.database.dao.Category> dbCategories = categoryMapper.fromCategories(categories);
                categoryDao.insertOrReplaceInTx(dbCategories);
                subscriber.onNext(categoryMapper.toCategories(dbCategories));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Category> save(Category category) {
        return Observable.create(subscriber -> {
            try {
                if (category.getId() == null) {
                    category.setId(String.valueOf(System.currentTimeMillis()));
                }
                com.outlay.database.dao.Category daoCategory = categoryMapper.fromCategory(category);

                categoryDao.insertOrReplace(daoCategory);
                subscriber.onNext(categoryMapper.toCategory(daoCategory));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Category> remove(Category category) {
        return Observable.create(subscriber -> {
            try {
                DeleteQuery<com.outlay.database.dao.Expense> deleteQuery = expenseDao.queryBuilder()
                        .where(ExpenseDao.Properties.CategoryId.eq(category.getId()))
                        .buildDelete();
                deleteQuery.executeDeleteWithoutDetachingEntities();

                categoryDao.deleteByKey(Long.valueOf(category.getId()));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
