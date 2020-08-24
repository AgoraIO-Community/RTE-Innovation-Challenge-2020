package com.young.businessmvvm.data.repository.network

import com.beile.app.view.mvvm.data.repository.network.IRemoteRequest
import com.framing.baselib.TLog
import com.framing.commonlib.BuildConfig
import com.framing.commonlib.network.RequestBuild

/*
 * Des  网络请求的mvvm管理
 * Author Young
 * Date 2020-05-13
 * 仓库角色 网络
 */
class NetRequestManager : RequestBuild, IRemoteRequest {

    @Volatile
    private var instance: NetRequestManager? = null
    private var reuqestApiInterface: IReuqestApi = retrofit.create(IReuqestApi::class.java)

    constructor() : super("http://mt2.google.cn" + "/") {//baseurl 截取第一个/之前的 结尾必须为/
    }

    fun getInstance(): NetRequestManager? {
        if (instance == null) {
            synchronized(NetRequestManager::class.java) {
                if (instance == null) {
                    instance = NetRequestManager()
                }
            }
        }
        return instance
    }
    fun getMap(){
        val a=reuqestApiInterface.getMap()
        apiSubscribe(a){
            TLog.log("okhttp111:$it")
        }
    }
}
