package com.azazellj.githubwatcher.data;

import com.azazellj.githubwatcher.BuildConfig;
import com.azazellj.githubwatcher.data.response.RepositoryResponse;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by azazellj on 02.10.17.
 */

public interface ApiRestService {
    // TODO: 10/3/17 add `per_page` param to app settings
    @GET("search/repositories?per_page=10")
    Observable<RepositoryResponse> findRepositories(@Query("q") String query, @Query("page") int page);


    class Creator {
        private static final int TIMEOUT_CONNECTION = 10;

        /**
         * Default constructor.
         */
        private Creator() {
        }

        public static ApiRestService newRestService() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            httpClient.connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);

            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .baseUrl(" https://api.github.com/")
                    .client(httpClient.build())
                    .build();

            return retrofit.create(ApiRestService.class);
        }
    }
}
