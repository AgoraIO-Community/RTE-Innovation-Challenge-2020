package com.young.aac.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.framing.baselib.TLog
import com.framing.commonlib.base.CommonBaseActivity
import com.framing.commonlib.utils.BarUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel

/*
* BaseUIViewMode   UI 管理
* BaseDataViewModel  业务数据管理 可以未null
* */
abstract class MvvmBaseActivity <V : ViewDataBinding, UVM : BaseUIViewModel<V>,DVM:BaseDataViewModel?> :
    CommonBaseActivity {

    private var dataVM: DVM? = null//dataViewModel
    private var UIVM: UVM? = null//UIViewModel
    private var viewDataBinding: V? = null//绑定布局layout的操作binding
    protected val schduleScope=GlobalScope//携程在手说走试试

    constructor() : super() {
    }
    protected abstract fun getDataViewModel(): DVM?//dataViewModel 业务数据
    protected abstract fun getUIViewModel(): UVM//UIViewModel UI控制
    abstract val bindingVariable: Int//BR 绑定id
    @get:LayoutRes
    abstract val layoutId: Int//布局


    private fun byDataBindingAttach() {//绑定UiViewmode 到xml
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        UIVM = if (UIVM == null) getUIViewModel() else UIVM
        dataVM=if (dataVM == null) getDataViewModel() else dataVM
        if (bindingVariable > 0 && UIVM != null) {
            viewDataBinding?.setVariable(bindingVariable, UIVM)
        } else {
        }
        viewDataBinding?.setLifecycleOwner(this)
        //转化data load 状态为UIloading
        TLog.log("getDataVm1","it${ getDataVM()}")
        getDataVM()?.loadType?.observe(this, Observer {
            TLog.log("getDataVm2","it$it")
            getUIVM()?.loadType.postValue(it)
        })
    }
    // app 级别的持有ViewModelProvider
    protected  fun getAppViewModelProvider(): ViewModelProvider? {
        return (applicationContext as MvvmBaseApplication).getAppViewModelProvider(this)
    }
    /*
    * activity 所持有的viewmodel 可以被容器内fragment所持有使用
    * */
    protected  fun getActivityViewModelProvider(activity: AppCompatActivity): ViewModelProvider{
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory)
    }
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this,R.color.colorAccent,false)
        byDataBindingAttach()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            schduleScope.cancel()
        }catch (e:IllegalStateException){
        }
    }
    /*
    *暴露出去的binding 给activity 使用
    *  */
    fun getBinding() : V{
        return viewDataBinding!!
    }
    fun getUIVM():UVM{
        return UIVM!!
    }
    fun getDataVM():DVM?{
        return dataVM
    }
    fun showToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}