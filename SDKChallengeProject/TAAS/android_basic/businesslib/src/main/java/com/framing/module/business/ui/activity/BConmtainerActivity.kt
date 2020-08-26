package com.framing.module.business.ui.activity

import android.os.Bundle
import com.young.aac.base.MvvmBaseActivity
import com.framing.module.business.databinding.BContainerActivityBinding
import com.framing.module.business.ui.viewmodel.BConmtainerUIVM
import com.framing.module.business.data.viewmodel.BConmtainerDataVM
import com.framing.module.business.BR
import com.framing.module.business.R
/**
 * Des
 * Created by VULCAN on 2020-08-20 13:21:01
 */
class BConmtainerActivity :  MvvmBaseActivity<BContainerActivityBinding, BConmtainerUIVM,BConmtainerDataVM>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        getBinding().liviView.startPrepare()
    }
    override fun getUIViewModel(): BConmtainerUIVM {
        return getActivityViewModelProvider(this).get(BConmtainerUIVM::class.java)
    }
    override fun getDataViewModel(): BConmtainerDataVM? {
            return getActivityViewModelProvider(this).get(BConmtainerDataVM::class.java)!!
    }
    override val bindingVariable: Int
        get() = BR.bUIVM
    override val layoutId: Int
        get() = R.layout.b_container_activity
}