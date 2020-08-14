package com.framing.module.customer.ui.activity

import android.os.Bundle
import com.young.aac.base.MvvmBaseActivity
import com.framing.module.customer.databinding.CContainerActivityBinding
import com.framing.module.customer.ui.viewmodel.CContainerUIVM
import com.framing.module.customer.data.viewmodel.CContainerDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.R
/**
 * Des 容器类 控制分发 控制业务 相当于controller
 * Created by Young on 2020-08-11 19:15:07
 */
class CContainerActivity :  MvvmBaseActivity<CContainerActivityBinding, CContainerUIVM,CContainerDataVM>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding().mapView.onCreate(savedInstanceState)
        getBinding().mapView.initLoad()
    }
    override fun getUIViewModel(): CContainerUIVM {
        return getActivityViewModelProvider(this).get(CContainerUIVM::class.java)
    }
    override fun getDataViewModel(): CContainerDataVM? {
            return getActivityViewModelProvider(this).get(CContainerDataVM::class.java)!!
    }
    override val bindingVariable: Int
        get() = BR.cContainerUIVM
    override val layoutId: Int
        get() = R.layout.c_container_activity
}