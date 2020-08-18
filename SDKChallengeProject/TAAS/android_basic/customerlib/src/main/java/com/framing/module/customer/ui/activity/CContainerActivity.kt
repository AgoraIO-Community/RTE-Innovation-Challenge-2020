package com.framing.module.customer.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.os.postDelayed
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.framing.baselib.TLog
import com.framing.commonlib.inject.policy.PermissionPolicy
import com.framing.commonlib.utils.BarUtils
import com.young.aac.base.MvvmBaseActivity
import com.framing.module.customer.databinding.CContainerActivityBinding
import com.framing.module.customer.ui.viewmodel.CContainerUIVM
import com.framing.module.customer.data.viewmodel.CContainerDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.CustomerShareVM
import com.framing.module.customer.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Des 容器类 控制分发 控制业务 相当于app controller
 * Created by Young on 2020-08-11 19:15:07
 */
class CContainerActivity :  MvvmBaseActivity<CContainerActivityBinding, CContainerUIVM,CContainerDataVM>(){

    private var appShareVM:CustomerShareVM?=null
    private var contentNavCtrl: NavController?=null//主内容分发控制器
    private var startNavCtrl: NavController?=null//启动页分发控制器

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appShareVM=getAppViewModelProvider()?.get(CustomerShareVM::class.java)
        contentNavCtrl=findNavController(R.id.content_fragment_host)
        startNavCtrl=findNavController(R.id.launch_fragment_host)

        uiLogic()
        permissionRun(PermissionPolicy.P_LOC){
            //权限请求
        }
        GlobalScope?.launch{
            delay(5000)
            appShareVM?.isStartHide?.postValue(true) //执行完了
        }
    }

    /*
    * UI 执行逻辑是由 app share vm 控制分发 启动页相关 还是 主内容相关
    * */
    @SuppressLint("ResourceAsColor")
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
