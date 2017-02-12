package app.outlay.database.source;

import app.outlay.data.source.CategoryDataSource;
import app.outlay.database.adapter.CategoryDatabaseMapper;
import app.outlay.database.dao.CategoryDao;
import app.outlay.database.dao.ExpenseDao;
import app.outlay.database.dao.Expense;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.dao.query.DeleteQuery;
import rx.Observable;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public class CategoryDatabaseSource implements CategoryDataSource {
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
    public Observable<List<app.outlay.domain.model.Category>> getAll() {
        return Observable.create(subscriber -> {
            try {
                List<Category> categories = categoryDao.queryBuilder().orderAsc(CategoryDao.Properties.Order).list();
                List<app.outlay.domain.model.Category> result = categoryMapper.toCategories(categories);
                subscriber.onNext(result);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<app.outlay.domain.model.Category> getById(String id) {
        return Observable.create(subscriber -> {
            try {
                Category c = categoryDao.load(Long.valueOf(id));
                app.outlay.domain.model.Category category = categoryMapper.toCategory(c);
                subscriber.onNext(category);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<List<app.outlay.domain.model.Category>> updateOrder(List<app.outlay.domain.model.Category> categories) {
        return Observable.create(subscriber -> {
            try {
                List<Category> dbCategories = categoryMapper.fromCategories(categories);
                categoryDao.insertOrReplaceInTx(dbCategories);
                subscriber.onNext(categoryMapper.toCategories(dbCategories));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<app.outlay.domain.model.Category> save(app.outlay.domain.model.Category category) {
        return Observable.create(subscriber -> {
            try {
                if (category.getId() == null) {
                    category.setId(String.valueOf(System.currentTimeMillis()));
                }
                Category daoCategory = categoryMapper.fromCategory(category);

                categoryDao.insertOrReplace(daoCategory);
                subscriber.onNext(categoryMapper.toCategory(daoCategory));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<app.outlay.domain.model.Category> remove(app.outlay.domain.model.Category category) {
        return Observable.create(subscriber -> {
            try {
                DeleteQuery<Expense> deleteQuery = expenseDao.queryBuilder()
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
