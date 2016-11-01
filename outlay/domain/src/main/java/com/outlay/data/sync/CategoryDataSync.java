package com.outlay.data.sync;

import com.outlay.data.source.CategoryDataSource;
import com.outlay.domain.model.Category;

import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 11/1/16.
 */

public class CategoryDataSync extends DataSync<Category, SyncSource<Category>> {
    public CategoryDataSync(CategoryDataSource localDataSource, CategoryDataSource remoteDataSource) {
        super(new CategorySyncSource(localDataSource), new CategorySyncSource(remoteDataSource));
    }

    private static class CategorySyncSource implements SyncSource<Category> {
        private CategoryDataSource categoryDataSource;

        public CategorySyncSource(CategoryDataSource categoryDataSource) {
            this.categoryDataSource = categoryDataSource;
        }

        @Override
        public Observable<List<Category>> getAll() {
            return categoryDataSource.getAll();
        }

        @Override
        public Observable<List<Category>> saveAll(List<Category> items) {
            return categoryDataSource.updateAll(items);
        }

        @Override
        public Observable<Void> clear() {
            return categoryDataSource.clear();
        }
    }
}
