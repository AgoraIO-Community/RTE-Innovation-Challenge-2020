package com.framing.module.customer.ui.viewmodel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.framing.baselib.TLog
import com.framing.module.customer.databinding.MapFragmentLayoutBinding
import com.young.aac.base.BaseUIViewModel

/**
 * Des
 * Created by VULCAN on 2020-08-13 13:30:55
 */
class MapUIVM : BaseUIViewModel<MapFragmentLayoutBinding> {
    constructor() : super(){
    }
    var isShowDialog=MutableLiveData<Boolean>()
}