package com.framing.commonlib.network

import com.framing.baselib.TLog
import com.framing.commonlib.network.interceptor.RequestInterceptor
import com.framing.commonlib.network.interceptor.ResponseInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
 * Des  
 * Author Young
 * Date 
 */open class RequestBuild  {

    protected val retrofit:Retrofit
    private var networkRequestInfo: IRequestHeaderInfo? = null
    private var sHttpsRequestInterceptor: RequestInterceptor? = null
    private var sHttpsResponseInterceptor: ResponseInterceptor? = null

    constructor(baseUrl:String){
        retrofit = Retrofit.Builder()
            .client(buildOk())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    /*
    * 构建okhtt
    * 参数 见名思意吧
    * */
    private fun buildOk():OkHttpClient{
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
        /*可以统一添加网络参数到请求头*/

        /*可以统一添加网络参数到请求头*/
        if (sHttpsRequestInterceptor != null) {
            okHttpClient.addInterceptor(sHttpsRequestInterceptor!!)
        }
        if (sHttpsResponseInterceptor != null) {
            /*网络请求返回的时候的数据处理*/
            okHttpClient.addInterceptor(sHttpsResponseInterceptor!!)
        }
        setLoggingLevel(okHttpClient)
        val httpClient = okHttpClient.build()
        httpClient.dispatcher.maxRequestsPerHost = 20
        return httpClient
    }
    /*
    * 构建请求打印
    * */
    private fun setLoggingLevel(builder: OkHttpClient.Builder) {
        val logging = HttpLoggingInterceptor()
        //BODY打印信息,NONE不打印信息
        logging.level=(if (true) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        builder.addInterceptor(logging)
    }

    fun setNetworkRequestInfo(requestInfo: IRequestHeaderInfo?) {
        networkRequestInfo = requestInfo
        sHttpsRequestInterceptor = RequestInterceptor(requestInfo)
        sHttpsResponseInterceptor = ResponseInterceptor()
    }
}