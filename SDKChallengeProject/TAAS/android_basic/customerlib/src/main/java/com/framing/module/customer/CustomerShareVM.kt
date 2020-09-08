package com.framing.module.customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/*
* 用户端 app  级别 数据分享
* */
class CustomerShareVM : ViewModel {
    constructor() : super(){
        isStartHide.postValue(false)//默认为启动状态 加载启动
    }

    var isStartHide=MutableLiveData<Boolean>()

}