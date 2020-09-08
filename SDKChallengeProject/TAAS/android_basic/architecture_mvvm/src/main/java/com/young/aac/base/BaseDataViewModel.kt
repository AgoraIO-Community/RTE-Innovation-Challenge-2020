package com.young.aac.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.framing.commonlib.network.error.LoadType
import com.framing.commonlib.widget.StatusLayout

/*
* 基础的数据管理可以下沉到这
* */
abstract  class BaseDataViewModel : ViewModel {
    constructor() : super(){
        loadType.postValue(StatusLayout.StatusType.HIDE)
    }
    var loadType=MutableLiveData<StatusLayout.StatusType>()//请求加载状态
}