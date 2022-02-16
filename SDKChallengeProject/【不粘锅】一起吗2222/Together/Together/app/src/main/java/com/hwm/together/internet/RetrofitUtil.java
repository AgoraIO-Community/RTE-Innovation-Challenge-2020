package com.hwm.together.internet;

import com.hwm.together.util.ConstantUtil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private static RetrofitUtil retrofitUtil;
    private static Retrofit mRetrofit;
    private RetrofitUtil(){}
    public static synchronized RetrofitUtil getInstance(){
        if (retrofitUtil == null){
            retrofitUtil = new RetrofitUtil();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ConstantUtil.HTTPURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitUtil;
    }
    public ApiServer getApiServer(){
        if (mRetrofit == null){
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ConstantUtil.HTTPURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        ApiServer apiServer = mRetrofit.create(ApiServer.class);
        return apiServer;
    }
}
