package com.example.rrty6.vcardapp.data.network;

import com.example.rrty6.vcardapp.utils.AppConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Initialization and configuration Retrofit
public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(AppConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());//Work with json

    public static <S> S createService(Class<S> serviceClass) {

        //For comfortable logging
        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        Retrofit retrofit = builder
                .client(httpClient.build())
                .build();
        return retrofit.create(serviceClass);
    }

}
