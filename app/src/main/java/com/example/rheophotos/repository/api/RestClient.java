package com.example.rheophotos.repository.api;

import com.example.rheophotos.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static RestClient sInstance;
    private Retrofit retrofit;

    private RestClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor(new NetworkInterceptor());

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static synchronized RestClient getInstance() {
        if (sInstance == null)
            sInstance = new RestClient();
        return sInstance;
    }

    public Retrofit getRestClient() {
        return retrofit;
    }
}
