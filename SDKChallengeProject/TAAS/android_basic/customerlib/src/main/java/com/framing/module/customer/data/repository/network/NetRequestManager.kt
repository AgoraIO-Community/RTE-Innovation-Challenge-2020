package com.young.businessmvvm.data.repository.network

import com.beile.app.view.mvvm.data.repository.network.IRemoteRequest
import com.framing.baselib.TLog
import com.framing.commonlib.BuildConfig
import com.framing.commonlib.network.BaseRequestApi
import com.framing.commonlib.network.RequestBuild
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.logging.Handler
import kotlin.coroutines.CoroutineContext

/*
 * Des  网络请求的mvvm管理
 * Author Young
 * Date 2020-05-13
 * 仓库角色 网络
 */
class NetRequestManager : RequestBuild, IRemoteRequest , CoroutineScope {

    private var reuqestApiInterface: IReuqestApi = retrofit.create(IReuqestApi::class.java)

    constructor() : super("http://www.anshunfeng.com.cn" + "/") {//baseurl 截取第一个/之前的 结尾必须为/
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
    fun getMap(){
        TLog.log("okhttp11111111:","1234")
        GlobalScope?.launch {
            val a=reuqestApiInterface.getMap()
            TLog.log("okhttp111:$a","\"1234\"")
        }

    }
}
