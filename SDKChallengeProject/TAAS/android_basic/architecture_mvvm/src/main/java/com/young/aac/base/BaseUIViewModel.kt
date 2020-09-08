package com.young.aac.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.framing.commonlib.network.error.LoadType
<<<<<<< HEAD
import com.framing.commonlib.widget.StatusLayout
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5

/*
* 基础的UI管理可以下沉到这
* */
abstract class BaseUIViewModel<T:ViewDataBinding> :ViewModel {
<<<<<<< HEAD
    constructor() : super(){
        loadType.postValue(StatusLayout.StatusType.HIDE)
    }
    var loadType= MutableLiveData<StatusLayout.StatusType>()//请求加载状态 观察datavm 状态获得
    protected var binding: T?=null//持有个binding 便于处理xml 无法处理逻辑
    protected var owner: LifecycleOwner?=null

=======
    constructor() : super()
    var loadType= MutableLiveData<LoadType>()//请求加载状态 观察datavm 状态获得
    protected var binding: T?=null//持有个binding 便于处理xml 无法处理逻辑
    protected var owner: LifecycleOwner?=null
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
    fun withBinding(binding: T,owner: LifecycleOwner){
        this.binding=binding
    }
}