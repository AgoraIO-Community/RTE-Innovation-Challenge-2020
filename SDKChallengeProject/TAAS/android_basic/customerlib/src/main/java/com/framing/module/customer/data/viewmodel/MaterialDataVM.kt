package com.framing.module.customer.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.framing.baselib.TLog
import com.framing.commonlib.widget.StatusLayout
import com.young.aac.base.BaseDataViewModel
import com.young.businessmvvm.data.repository.network.NetRequestManager
import kotlinx.coroutines.launch

/**
 * Des
 * Created by VULCAN on 2020-09-05 13:30:33
 */
class MaterialDataVM : BaseDataViewModel{
    constructor() : super()

    var showContent=MutableLiveData<String>()

    fun requestData(){
        loadType.postValue(StatusLayout.StatusType.LOADING)
        viewModelScope.launch {
            val ob= NetRequestManager.get().reuqestApiInterface.getMaterial()
            NetRequestManager.get().getResult (ob,this@MaterialDataVM){
                TLog.log("test_request",it.toString())
                showContent.postValue(it)
            }
        }
    }

}