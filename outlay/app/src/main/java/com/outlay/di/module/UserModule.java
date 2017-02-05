package com.outlay.di.module;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outlay.data.repository.CategoryRepositoryImpl;
import com.outlay.data.repository.ExpenseRepositoryImpl;
import com.outlay.data.sync.DataSync;
import com.outlay.database.source.CategoryDatabaseSource;
import com.outlay.database.source.ExpenseDatabaseSource;
import com.outlay.di.scope.UserScope;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.Expense;
import com.outlay.domain.model.OutlaySession;
import com.outlay.domain.model.User;
import com.outlay.domain.repository.CategoryRepository;
import com.outlay.domain.repository.ExpenseRepository;
import com.outlay.firebase.CategoryFirebaseSource;
import com.outlay.firebase.ExpenseFirebaseSource;

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
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        if (!user.isAnonymous()) {
            database.child("users").child(user.getId()).keepSynced(true);
        }
        return database;
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
            CategoryFirebaseSource firebaseSource
    ) {
        return new CategoryRepositoryImpl(
                databaseSource,
                firebaseSource
        );
    }

    @Provides
    @UserScope
    public ExpenseRepository provideExpenseRepository(
            ExpenseFirebaseSource expenseDataSource,
            ExpenseDatabaseSource databaseSource
    ) {
        return new ExpenseRepositoryImpl(
                databaseSource,
                expenseDataSource
        );
    }

    @Provides
    @UserScope
    public DataSync<Category> provideCategorySync(
            CategoryFirebaseSource firebaseSource,
            CategoryDatabaseSource databaseSource
    ) {
        return new DataSync(
                databaseSource,
                firebaseSource
        );
    }

    @Provides
    @UserScope
    public DataSync<Expense> provideExpenseSync(
            ExpenseFirebaseSource firebaseSource,
            ExpenseDatabaseSource databaseSource
    ) {
        return new DataSync<>(
                databaseSource,
                firebaseSource
        );
    }
}
