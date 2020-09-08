package com.young.businessmvvm.data.repository.network

<<<<<<< HEAD
import com.framing.module.customer.data.RecordBean
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
import com.framing.module.customer.data.bean.SimpleDataBean
import com.framing.module.customer.data.bean.TestBean
import com.young.bean.PageAllBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url


/*
 * Des Retrofit 构造请求
 * Author Young
 *
 */interface IReuqestApi {

<<<<<<< HEAD
    @GET("/taas_app/app/page_all")
    fun getPageAllData( ) : Observable<SimpleDataBean<PageAllBean>>

    @GET("/taas_app/app/page_material")
    fun getMaterial( ) : Observable<SimpleDataBean<String>>

    @GET("/taas_app/app/record_video")
    fun getRecord( ) : Observable<SimpleDataBean<List<RecordBean>>>

    //下载谷歌瓦片
=======
    @GET("/taas_brain/app/mappage")
    fun getPageAllData( ) : Observable<SimpleDataBean<PageAllBean>>
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
    @Streaming
    @GET
    fun downloadMap(@Url fileUrl: String?): Call<ResponseBody?>?


}