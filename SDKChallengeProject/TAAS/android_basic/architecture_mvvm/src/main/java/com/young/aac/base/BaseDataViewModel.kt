package com.young.aac.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.framing.commonlib.network.error.LoadType
<<<<<<< HEAD
import com.framing.commonlib.widget.StatusLayout
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5

/*
* 基础的数据管理可以下沉到这
* */
abstract  class BaseDataViewModel : ViewModel {
<<<<<<< HEAD
    constructor() : super(){
        loadType.postValue(StatusLayout.StatusType.HIDE)
    }
    var loadType=MutableLiveData<StatusLayout.StatusType>()//请求加载状态
=======
    constructor() : super()
    var loadType=MutableLiveData<LoadType>()//请求加载状态
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
}