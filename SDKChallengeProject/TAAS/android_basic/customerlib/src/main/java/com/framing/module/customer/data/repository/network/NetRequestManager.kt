package com.young.businessmvvm.data.repository.network

import com.beile.app.view.mvvm.data.repository.network.IRemoteRequest
import com.framing.baselib.TLog
import com.framing.commonlib.network.BaseRequestApi
import com.framing.commonlib.network.IRequestHeaderInfo
import com.framing.commonlib.network.error.ExceptionError
import com.framing.commonlib.network.error.LoadType
import com.framing.commonlib.network.observer.BaseObserver
import com.framing.module.customer.data.bean.SimpleDataBean
import com.young.aac.base.BaseDataViewModel
import io.reactivex.Observable
import kotlinx.coroutines.*
import java.io.InputStream
import java.util.logging.Handler
import kotlin.coroutines.CoroutineContext

/*
 * Des  网络请求的mvvm管理
 * Author Young
 * 仓库角色 网络
 */
class NetRequestManager : BaseRequestApi, IRemoteRequest {

    var reuqestApiInterface: IReuqestApi = retrofit.create(IReuqestApi::class.java)

    constructor() : super("http://182.92.164.242:8080" + "/") {//baseurl 截取第一个/之前的 结尾必须为/
    }
    companion object {
        private var instance: NetRequestManager? = null
            get() {
                if (field == null) {
                    field = NetRequestManager()
                }
                return field
            }
        @Synchronized
        fun get(): NetRequestManager{
            return instance!!
        }
    }

    override fun setNetworkRequestInfo(requestInfo: IRequestHeaderInfo?) {
        super.setNetworkRequestInfo(requestInfo)
    }
    /*
    * 小封装一下
    * 加载状态的基本请求形态
    * */
    fun <T>getResult(
        data:Observable<SimpleDataBean<T>>,
        dataVM: BaseDataViewModel,
        mt:(T)->Unit){
        ApiSubscribe(data, object : BaseObserver<SimpleDataBean<T>>() {
            override fun onError(e: ExceptionError.ResponeThrowable?) {
                TLog.log("netrequest_onerror","${e.toString()}")
                dataVM.loadType.postValue(LoadType.NET_ERROR)
            }
            override fun onNext(t: SimpleDataBean<T>) {
                TLog.log("netrequest_onNext","${t}")
                if(t.code==0){
                    mt(t.data)
                }else{
                    dataVM.loadType.postValue(LoadType.SERVER_FAIL)
                }
            }
        })
    }
    fun getMap(url:String):InputStream{
      var a=  reuqestApiInterface.downloadMap(url)
        return a?.execute()?.body()?.byteStream()!!
    }
}
