package com.young.businessmvvm.data.repository.network

import com.framing.module.customer.data.bean.SimpleDataBean
import com.framing.module.customer.data.bean.TestBean
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

    @POST("/asfbusiness/user/info")
    suspend  fun getMap( ) : SimpleDataBean<TestBean>

}