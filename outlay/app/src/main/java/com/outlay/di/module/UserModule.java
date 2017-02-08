package com.outlay.di.module;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outlay.data.repository.CategoryRepositoryImpl;
import com.outlay.data.repository.ExpenseRepositoryImpl;
import com.outlay.data.source.CategoryDataSource;
import com.outlay.data.source.ExpenseDataSource;
import com.outlay.di.scope.UserScope;
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
    public User provideCurrentUser(
    ) {
        return user;
    }

    @Provides
    @UserScope
    public DatabaseReference provideDatabseRef(
    ) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(user.getId()).keepSynced(true);
        return database;
    }

    @Provides
    @UserScope
    public CategoryDataSource provideFirebaseCategoryDataSource(
            DatabaseReference databaseReference
    ) {
        return new CategoryFirebaseSource(user, databaseReference);
    }

    @Provides
    @UserScope
    public ExpenseDataSource provideExpenseFirebaseSource(
            DatabaseReference databaseReference,
            CategoryDataSource categoryFirebaseSource
    ) {
        return new ExpenseFirebaseSource(user, databaseReference, categoryFirebaseSource);
    }

    @Provides
    @UserScope
    public CategoryRepository provideCategoryRepository(CategoryDataSource firebaseSource) {
        return new CategoryRepositoryImpl(firebaseSource);
    }

    @Provides
    @UserScope
    public ExpenseRepository provideExpenseRepository(ExpenseDataSource expenseDataSource) {
        return new ExpenseRepositoryImpl(expenseDataSource);
    }
}
