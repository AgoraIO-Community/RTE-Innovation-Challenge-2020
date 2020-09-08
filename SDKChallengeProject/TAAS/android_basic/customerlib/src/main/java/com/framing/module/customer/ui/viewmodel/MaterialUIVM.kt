package com.framing.module.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.framing.module.customer.databinding.FragmentMaterialLayoutBinding
import com.young.aac.base.BaseUIViewModel

/**
 * Des
 * Created by VULCAN on 2020-09-05 13:30:33
 */
class MaterialUIVM : BaseUIViewModel<FragmentMaterialLayoutBinding>{
    constructor() : super()
    var showContent= MutableLiveData<String>()
}