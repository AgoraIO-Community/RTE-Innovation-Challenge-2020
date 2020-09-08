package com.framing.module.customer.ui.viewmodel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.framing.baselib.TLog
import com.framing.module.customer.data.bean.WeatherLiveData
import com.framing.module.customer.databinding.MapFragmentLayoutBinding
import com.young.aac.base.BaseUIViewModel
import com.young.bean.ContentListBean

/**
 * Des 构造viewmodel 的每条属性一定要遵循单条单用机制
 * 切勿一条多用 会造成状态错乱越来越难以维护的情况
 * Created by VULCAN on 2020-08-13 13:30:55
 */
class MapUIVM : BaseUIViewModel<MapFragmentLayoutBinding> {
    constructor() : super(){
    }
    var isShowDialog=MutableLiveData<Boolean>()//dialog指令
    var contentStatus=MutableLiveData<Int>()//top content 1 miss  2   half 3  all

    //加载righttip science
    fun loadScienceTip(data:ContentListBean){
        binding?.rightOne?.setSimpleData(data)
    }
    //加载righttip focus
    fun loadFocusTip(data:ContentListBean){
        binding?.rightThree?.setSimpleData(data)
    }
    //加载righttip weather
    fun loadWeatherTip(data:WeatherLiveData){
        binding?.rightTwo?.setWeatherData(data)
    }
}