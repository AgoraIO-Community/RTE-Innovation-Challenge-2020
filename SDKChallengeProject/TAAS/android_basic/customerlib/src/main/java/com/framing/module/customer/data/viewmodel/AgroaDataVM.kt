package com.framing.module.customer.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.framing.baselib.TLog
import com.framing.module.customer.data.RecordBean
import com.young.aac.base.BaseDataViewModel
import com.young.businessmvvm.data.repository.network.NetRequestManager
import kotlinx.coroutines.launch

/**
 * Des
 * Created by VULCAN on 2020-09-05 23:32:14
 */
class AgroaDataVM : BaseDataViewModel{
    constructor() : super()
    var recordDatas= MutableLiveData<List<RecordBean>>()

    //视频列表
    fun requestData(){
        viewModelScope.launch {
            val ob= NetRequestManager.get().reuqestApiInterface.getRecord()
            NetRequestManager.get().getResult (ob,this@AgroaDataVM){
                TLog.log("test_request",it.toString())
                recordDatas.postValue(it)
            }
        }
    }
}