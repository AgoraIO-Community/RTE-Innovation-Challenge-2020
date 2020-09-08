package com.framing.module.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.framing.module.customer.databinding.CContainerActivityBinding
import com.young.aac.base.BaseUIViewModel

/**
 * Des
 * Created by VULCAN on 2020-08-11 19:15:07
 */
class CContainerUIVM : BaseUIViewModel<CContainerActivityBinding>{
    constructor() : super(){
        isStartHide.postValue(false)//默认为启动状态 加载启动
    }
    val isStartHide=MutableLiveData<Boolean>()//控制启动页 和首页 切换
    val isDialogShow=MutableLiveData<Boolean>()//控制弹窗显示
<<<<<<< HEAD
    var isGoClick=MutableLiveData<Boolean>()//控制弹窗点击暴露给fragment
    var isConainerBgShow=MutableLiveData<Boolean>()//container bg 切换背景
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
}