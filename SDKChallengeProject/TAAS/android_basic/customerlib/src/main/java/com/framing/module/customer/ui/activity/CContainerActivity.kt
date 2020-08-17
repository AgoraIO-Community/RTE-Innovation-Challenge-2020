package com.framing.module.customer.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.os.postDelayed
import androidx.lifecycle.Observer
import com.framing.baselib.TLog
import com.framing.commonlib.inject.policy.PermissionPolicy
import com.young.aac.base.MvvmBaseActivity
import com.framing.module.customer.databinding.CContainerActivityBinding
import com.framing.module.customer.ui.viewmodel.CContainerUIVM
import com.framing.module.customer.data.viewmodel.CContainerDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.CustomerShareVM
import com.framing.module.customer.R
/**
 * Des 容器类 控制分发 控制业务 相当于controller
 * Created by Young on 2020-08-11 19:15:07
 */
class CContainerActivity :  MvvmBaseActivity<CContainerActivityBinding, CContainerUIVM,CContainerDataVM>(){

    private var appShareVM:CustomerShareVM?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appShareVM=getAppViewModelProvider()?.get(CustomerShareVM::class.java)
        uiLogic()
        permissionRun(PermissionPolicy.P_LOC){

        }
        TLog.log("isStartHide","${appShareVM?.isStartHide?.value} ___ ${getUIViewModel().isStartHide.value}")
        Handler().postDelayed(Runnable {
            appShareVM?.isStartHide?.postValue(true) //执行完了
            TLog.log("isStartHide","1111")
        },5000)
//        getBinding().mapView.onCreate(savedInstanceState)
//        getBinding().mapView.initLoad()
    }

    /*
    * UI 执行逻辑是由 app share vm 控制分发 启动页相关 还是 主内容相关
    * */
    private fun uiLogic() {
        appShareVM?.isStartHide?.observe(this, Observer {
            TLog.log("isStartHide","2222$it ___"+(getUIViewModel().isStartHide.value!=it))
            if(getUIViewModel().isStartHide.value!=it) {//UI 状态 和 data 不一致才处理
                getUIViewModel().isStartHide.postValue(true)
                TLog.log("isStartHide","333${getUIViewModel().isStartHide.value} ___")
            }
        })
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
