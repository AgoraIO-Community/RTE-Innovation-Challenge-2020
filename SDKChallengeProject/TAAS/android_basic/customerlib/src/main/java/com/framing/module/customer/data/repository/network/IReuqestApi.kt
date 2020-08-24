package com.young.businessmvvm.data.repository.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import java.util.*

/*
 * Des Retrofit 构造请求
 * Author Young
 * Date 2020-05-21
 */interface IReuqestApi {

    @GET("/vt/lyrs=y&scale=2&hl=zh-CN&gl=cn&x=%d&s=&y=%d&z=%d")
    fun getMap( ) : Object

}