package com.framing.module.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.young.aac.base.BaseUIViewModel

/**
 * Des
 * Created by VULCAN on 2020-08-11 19:15:07
 */
class CContainerUIVM : BaseUIViewModel{
    constructor() : super(){
        isStartHide.postValue(false)//默认为启动状态 加载启动
    }
    val isStartHide=MutableLiveData<Boolean>()
}