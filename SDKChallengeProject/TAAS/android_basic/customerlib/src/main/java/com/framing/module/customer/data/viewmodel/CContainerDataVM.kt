package com.framing.module.customer.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.framing.baselib.TLog
import com.framing.commonlib.helper.ProjectLiveData
import com.young.aac.base.BaseDataViewModel
import com.young.bean.BottomBean
import com.young.bean.ContentListBean
import com.young.bean.PageAllBean
import com.young.businessmvvm.data.repository.network.NetRequestManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Des
 * Created by VULCAN on 2020-08-11 19:15:07
 */
class CContainerDataVM : BaseDataViewModel{
    constructor() : super()
    /*
    * pageAllBean 请求回来的数据
    * 分配好的数据
    * bottomData 底部数据
    * scienceData righttip
    * focusData righttip
    * isShowDialog 强展示dialog
    * dialogLev 弹窗级别
    *
    * */
    var pageAllBean=ProjectLiveData<PageAllBean>()
    var bottomData=ProjectLiveData<BottomBean>()
    var scienceData=ProjectLiveData<ContentListBean>()
    var focusData=ProjectLiveData<ContentListBean>()
    var isShowDialog=MutableLiveData<Boolean>()
    var dialogLev=MutableLiveData<Int>()


    fun requestData(){
        viewModelScope.launch {
            val ob=NetRequestManager.get().reuqestApiInterface.getPageAllData()
            NetRequestManager.get().getResult (ob,this@CContainerDataVM){
                TLog.log("test_request",it.toString())
            }
        }

    }
}