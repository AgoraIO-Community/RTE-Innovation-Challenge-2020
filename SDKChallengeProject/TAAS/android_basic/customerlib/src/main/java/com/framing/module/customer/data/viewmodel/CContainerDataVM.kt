package com.framing.module.customer.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.framing.baselib.TLog
import com.framing.commonlib.helper.ProjectLiveData
import com.framing.commonlib.widget.StatusLayout
import com.framing.module.customer.data.bean.WeatherLiveData
import com.young.aac.base.BaseDataViewModel
import com.young.bean.BottomBean
import com.young.bean.ContentListBean
import com.young.bean.DialogBean
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
<<<<<<< HEAD
    * weatherData 实时天气数据
    * weaFutureData 未来天气数据
=======
    *
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
    * */
    var pageAllBean=ProjectLiveData<PageAllBean>()
    var bottomData=ProjectLiveData<BottomBean>()
    var scienceData=ProjectLiveData<ContentListBean>()
    var focusData=ProjectLiveData<ContentListBean>()
    var isShowDialog=MutableLiveData<Boolean>()
    var dialogLev=MutableLiveData<Int>()
    var dialogData=MutableLiveData<DialogBean>()
    var weatherData=MutableLiveData<WeatherLiveData>()
    var weaFutureData=MutableLiveData<WeatherLiveData>()

    /*
    * 请求首页全部
    * */
    fun requestData(){
        loadType.postValue(StatusLayout.StatusType.LOADING)
        viewModelScope.launch {
            val ob=NetRequestManager.get().reuqestApiInterface.getPageAllData()
            NetRequestManager.get().getResult (ob,this@CContainerDataVM){
                TLog.log("test_request",it.toString())
                bottomData.postValue(it.bottom)
                scienceData.postValue(it.science)
                focusData.postValue(it.focus)
                isShowDialog.postValue(it.isShowDialog)
                dialogLev.postValue(it.dialogLevel)
                val dialog=it.dialogBean
                dialog.dialogLevel=1
                dialog.isShowDialog=true
                dialogData.postValue(dialog)
            }
        }
    }
}