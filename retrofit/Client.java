package com.developer.webservice.retrofit;

import android.support.annotation.NonNull;


import com.developer.webservice.reusables.SmartApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class Client {


    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(new ConnectivityInterceptor(SmartApplication.getInstance()))
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request original = chain.request();
                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Cache-Control", "no-cache")
                            .addHeader("Cache-Control", "no-store");

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            })
            .build();

    static Retrofit getRetrofitClient(final Object tag, final String headerKey, final String headerValue) {

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .baseUrl(Const.BASE_URL)
                .callFactory(new Call.Factory() {
                    @NonNull
                    @Override
                    public Call newCall(@NonNull Request request) {
                        Request.Builder builder = request.newBuilder();
                        if (headerKey != null && headerValue != null) {
                            builder.addHeader(headerKey, headerValue);
                        }
                        builder.tag(tag);

                        return client.newCall(builder.build());
                    }
                })
                .build();
    }

    static OkHttpClient getClient() {
        return client;
    }

}
