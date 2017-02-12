package app.outlay.firebase;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import app.outlay.data.source.CategoryDataSource;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.User;
import app.outlay.firebase.dto.CategoryDto;
import app.outlay.firebase.dto.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class CategoryFirebaseSource implements CategoryDataSource {
    private DatabaseReference mDatabase;
    private CategoryAdapter adapter;
    private User currentUser;

    public CategoryFirebaseSource(
            User currentUser,
            DatabaseReference databaseReference
    ) {
        this.currentUser = currentUser;
        mDatabase = databaseReference;
        adapter = new CategoryAdapter();
    }

    @Override
    public Observable<List<Category>> getAll() {
        return Observable.create(subscriber -> {
            mDatabase.child("users").child(currentUser.getId()).child("categories").orderByChild("order")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Category> categories = new ArrayList<>();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                CategoryDto categoryDto = postSnapshot.getValue(CategoryDto.class);
                                categories.add(adapter.toCategory(categoryDto));
                            }
                            subscriber.onNext(categories);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });


    }

    @Override
    public Observable<Category> getById(String id) {
        return getDtoById(id).map(categoryDto -> adapter.toCategory(categoryDto));
    }

    protected Observable<CategoryDto> getDtoById(String id) {
        return Observable.create(subscriber -> {
            mDatabase.child("users").child(currentUser.getId()).child("categories").child(id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CategoryDto categoryDto = dataSnapshot.getValue(CategoryDto.class);
                            subscriber.onNext(categoryDto);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    @Override
    public Observable<List<Category>> updateOrder(List<Category> categories) {
        return Observable.create(subscriber -> {
            Map<String, Object> childUpdates = new HashMap<>();
            for (Category c : categories) {
                childUpdates.put(c.getId() + "/order", c.getOrder());
            }

            DatabaseReference categoriesRef = mDatabase.child("users").child(currentUser.getId()).child("categories");
            categoriesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subscriber.onNext(categories);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
            categoriesRef.updateChildren(childUpdates);
        });
    }

    @Override
    public Observable<Category> save(Category category) {
        Observable<Category> saveCategory = Observable.create(subscriber -> {
            String key = category.getId();
            if (TextUtils.isEmpty(key)) {
                key = mDatabase.child("users").child(currentUser.getId()).child("categories").push().getKey();
                category.setId(key);
            }

            //TODO move this
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("id", category.getId());
            childUpdates.put("title", category.getTitle());
            childUpdates.put("icon", category.getIcon());
            childUpdates.put("color", category.getColor());
            childUpdates.put("order", category.getOrder());

            DatabaseReference dbRef = mDatabase
                    .child("users")
                    .child(currentUser.getId())
                    .child("categories").child(key);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CategoryDto categoryDto = dataSnapshot.getValue(CategoryDto.class);
                    if (categoryDto != null) {
                        subscriber.onNext(adapter.toCategory(categoryDto));
                        subscriber.onCompleted();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });

            dbRef.updateChildren(childUpdates);
        });
        return saveCategory;
    }

    @Override
    public Observable<Category> remove(Category category) {
        final Observable<Category> deleteCategory = Observable.create(subscriber -> {
            DatabaseReference catReference = mDatabase.child("users").child(currentUser.getId())
                    .child("categories").child(category.getId());
            catReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subscriber.onNext(category);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
            catReference.removeValue();
        });

        return deleteCategory;
    }
}
