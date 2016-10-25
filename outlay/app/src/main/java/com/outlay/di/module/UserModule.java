package com.outlay.di.module;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outlay.data.repository.ExpenseRepositoryImpl;
import com.outlay.database.source.CategoryDatabaseSource;
import com.outlay.di.scope.UserScope;
import com.outlay.domain.model.User;
import com.outlay.domain.repository.CategoryRepository;
import com.outlay.domain.repository.ExpenseRepository;
import com.outlay.firebase.CategoryFirebaseSource;
import com.outlay.firebase.ExpenseFirebaseSource;
import com.outlay.impl.CategoryRepositoryImpl;

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
        if (user == null) {
            return null;
        }
        return new CategoryFirebaseSource(user, databaseReference);
    }

    @Provides
    @UserScope
    public ExpenseFirebaseSource provideExpenseFirebaseSource(
            DatabaseReference databaseReference
    ) {
        if (user == null) {
            return null;
        }
        return new ExpenseFirebaseSource(user, databaseReference);
    }

    @Provides
    @UserScope
    public CategoryRepository provideCategoryRepository(
            CategoryDatabaseSource databaseSource,
            CategoryFirebaseSource firebaseSource,
            Context application
    ) {
        return new CategoryRepositoryImpl(databaseSource, firebaseSource, application);
    }

    @Provides
    @UserScope
    public ExpenseRepository provideExpenseRepository(
            ExpenseFirebaseSource expenseDataSource
    ) {
        return new ExpenseRepositoryImpl(expenseDataSource);
    }
}
