package com.young.aac.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.framing.commonlib.network.error.LoadType

/*
* 基础的UI管理可以下沉到这
* */
abstract class BaseUIViewModel :ViewModel {
    constructor() : super()
    var loadType= MutableLiveData<LoadType>()//请求加载状态 观察datavm 状态获得
}