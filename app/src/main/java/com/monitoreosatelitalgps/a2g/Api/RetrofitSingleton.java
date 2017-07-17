package com.monitoreosatelitalgps.a2g.Api;

import android.content.Context;
import com.monitoreosatelitalgps.a2g.BuildConfig;
import com.monitoreosatelitalgps.a2g.R;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sbv23 on 5/05/2017.
 */

public class RetrofitSingleton {

    private static Retrofit retrofit;
    private static ApiInterface api;

    private static OkHttpClient getClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(20, TimeUnit.SECONDS);
        httpClient.readTimeout(50, TimeUnit.SECONDS);
        return httpClient.build();
    }

    public static ApiInterface getApi(Context context){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(context.getString(R.string.URL))
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .validateEagerly(BuildConfig.DEBUG)
                    .build();
            api = retrofit.create(ApiInterface.class);
        }
        return api;
    }
}
