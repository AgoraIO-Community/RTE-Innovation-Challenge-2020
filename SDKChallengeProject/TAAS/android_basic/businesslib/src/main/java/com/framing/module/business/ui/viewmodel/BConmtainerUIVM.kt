package com.framing.module.business.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.framing.module.business.databinding.BContainerActivityBinding
import com.young.aac.base.BaseUIViewModel

/**
 * Des
 * Created by VULCAN on 2020-08-20 13:21:01
 */
class BConmtainerUIVM : BaseUIViewModel<BContainerActivityBinding>{
    constructor() : super(){
        isStartHide.postValue(false)//默认为启动状态 加载启动
    }

    val isStartHide= MutableLiveData<Boolean>()//控制启动页 和首页 切换

}