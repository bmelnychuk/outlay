package com.outlay.di.module;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outlay.data.repository.ExpenseRepositoryImpl;
import com.outlay.data.sync.CategoryDataSync;
import com.outlay.data.sync.ExpenseDataSync;
import com.outlay.database.source.CategoryDatabaseSource;
import com.outlay.database.source.ExpenseDatabaseSource;
import com.outlay.di.scope.UserScope;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.User;
import com.outlay.domain.repository.CategoryRepository;
import com.outlay.domain.repository.ExpenseRepository;
import com.outlay.firebase.CategoryFirebaseSource;
import com.outlay.firebase.ExpenseFirebaseSource;
import com.outlay.impl.CategoryRepositoryImpl;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bmelnychuk on 10/27/16.
 */

@Module
public class UserModule {
    private User user;

    public UserModule(User user) {
        this.user = user;
    }

    @Provides
    @UserScope
    public DatabaseReference provideDatabseRef(
    ) {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides
    @UserScope
    public CategoryFirebaseSource provideFirebaseCategoryDataSource(
            DatabaseReference databaseReference
    ) {
        return new CategoryFirebaseSource(user, databaseReference);
    }

    @Provides
    @UserScope
    public ExpenseFirebaseSource provideExpenseFirebaseSource(
            DatabaseReference databaseReference,
            CategoryFirebaseSource categoryFirebaseSource
    ) {
        return new ExpenseFirebaseSource(user, databaseReference, categoryFirebaseSource);
    }

    @Provides
    @UserScope
    public CategoryRepository provideCategoryRepository(
            CategoryDatabaseSource databaseSource,
            CategoryFirebaseSource firebaseSource,
            List<Category> defaultCategories
    ) {
        return new CategoryRepositoryImpl(
                databaseSource,
                user == null ? null : firebaseSource
        );
    }

    @Provides
    @UserScope
    public ExpenseRepository provideExpenseRepository(
            ExpenseFirebaseSource expenseDataSource,
            ExpenseDatabaseSource databaseSource
    ) {
        return new ExpenseRepositoryImpl(databaseSource, user == null ? null : expenseDataSource);
    }

    @Provides
    @UserScope
    public CategoryDataSync provideCategoryDataSync(
            CategoryDatabaseSource databaseSource,
            CategoryFirebaseSource firebaseSource
    ) {
        return new CategoryDataSync(databaseSource, firebaseSource);
    }

    @Provides
    @UserScope
    public ExpenseDataSync provideExpenseDataSync(
            ExpenseFirebaseSource expenseDataSource,
            ExpenseDatabaseSource databaseSource
    ) {
        return new ExpenseDataSync(databaseSource, expenseDataSource);
    }
}
