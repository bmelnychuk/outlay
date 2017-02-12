package app.outlay.di.module;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import app.outlay.data.repository.CategoryRepositoryImpl;
import app.outlay.data.repository.ExpenseRepositoryImpl;
import app.outlay.data.source.CategoryDataSource;
import app.outlay.data.source.ExpenseDataSource;
import app.outlay.di.scope.UserScope;
import app.outlay.domain.model.User;
import app.outlay.domain.repository.CategoryRepository;
import app.outlay.domain.repository.ExpenseRepository;
import app.outlay.firebase.CategoryFirebaseSource;
import app.outlay.firebase.ExpenseFirebaseSource;

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
