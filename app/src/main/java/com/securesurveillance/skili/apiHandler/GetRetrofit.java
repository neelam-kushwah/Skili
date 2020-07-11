package com.securesurveillance.skili.apiHandler;



import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRetrofit {

    public static Retrofit getInstance() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60000, TimeUnit.MILLISECONDS).addInterceptor(getLoggingInterceptor())
                .connectTimeout(60000, TimeUnit.MILLISECONDS)

                .build();
        return new Retrofit.Builder()
                .baseUrl(AppConstants.Urls.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

//    public static Retrofit getInstanceExtraTime() {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .readTimeout(90000, TimeUnit.MILLISECONDS)
//                .connectTimeout(90000, TimeUnit.MILLISECONDS)
//                .addInterceptor(getLoggingInterceptor())
//                .build();
//        return new Retrofit.Builder()
//                .baseUrl(AppConstants.Urls.BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }

    private static HttpLoggingInterceptor getLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }


}
