package com.outlay.firebase.rest;

import com.outlay.firebase.dto.CategoryDto;
import com.outlay.firebase.dto.ExpenseDto;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bmelnychuk on 11/1/16.
 */

public class Firebase {
    public static final String BASE_URL = "https://outlay-d5aef.firebaseio.com";
    private static final String CATEGORIES = "/users/{userId}/categories.json";
    private static final String EXPENSES = "/users/{userId}/expenses.json";

    public interface Api {
        @GET(CATEGORIES)
        Observable<Map<String, CategoryDto>> getCategoies(
                @Path(value = "userId") String userId,
                @Query("auth") String token
        );

        @GET(EXPENSES)
        Observable<Map<String, ExpenseDto>> getExpenses(
                @Path(value = "userId") String userId,
                @Query("auth") String token
        );
    }
}
