package com.framing.module.business.ui.viewmodel

import androidx.lifecycle.MutableLiveData
<<<<<<< HEAD
import com.framing.module.business.databinding.BContainerActivityBinding
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
import com.young.aac.base.BaseUIViewModel

/**
 * Des
 * Created by VULCAN on 2020-08-20 13:21:01
 */
<<<<<<< HEAD
class BConmtainerUIVM : BaseUIViewModel<BContainerActivityBinding>{
=======
class BConmtainerUIVM : BaseUIViewModel{
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
    constructor() : super(){
        isStartHide.postValue(false)//默认为启动状态 加载启动
    }

    val isStartHide= MutableLiveData<Boolean>()//控制启动页 和首页 切换

}