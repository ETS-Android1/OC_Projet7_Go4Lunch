package com.openclassrooms.go4lunch.di;

import android.content.Context;

import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.database.Go4LunchDatabase;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dependency injection class
 */
public class DI {

    /**
     * Returns an instance of the application database.
     * @param context : Context
     * @return : Go4Lunch SQLite Database
     */
    public static Go4LunchDatabase provideDatabase(Context context) {
        return Go4LunchDatabase.getInstance(context);
    }

    /**
     * Provides an instance of an Executor object to use to execute Runnable tasks outside
     * the main thread.
     * @return : Executor object
     */
    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }


    /**
     *
     * @return : Retrofit instance
     */
    public static Retrofit provideRetrofit() {

        // Define HTTP traffic interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add interceptor
        Interceptor requestInterceptor = chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("key", BuildConfig.API_KEY)
                    .addQueryParameter("rankby", "distance")
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };

        // Define HTTP client
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(httpClient) // Client HTTP
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
