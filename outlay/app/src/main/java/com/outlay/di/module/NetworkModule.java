package com.outlay.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.outlay.domain.repository.OutlayAuth;
import com.outlay.firebase.FirebaseAuthRxWrapper;
import com.outlay.firebase.rest.Firebase;
import com.outlay.impl.OutlayAuthImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bmelnychuk on 11/1/16.
 */
@Module
public class NetworkModule {
    @Provides
    @Singleton
    Gson providerGson() {
        Gson gson = new GsonBuilder().create();
        return gson;
    }

    @Provides
    @Singleton
    Firebase.Api providerFirebaseApi(Gson gson) {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Firebase.BASE_URL)
                .client(client)
                .build();
        return retrofit.create(Firebase.Api.class);
    }

    @Provides
    @Singleton
    public OutlayAuth provideOutlayAuth(
            FirebaseAuthRxWrapper firebaseRxWrapper
    ) {
        return new OutlayAuthImpl(firebaseRxWrapper);
    }
}
