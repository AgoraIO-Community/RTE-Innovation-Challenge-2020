package com.young.aac.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.framing.commonlib.base.CommonBaseActivity
import com.framing.commonlib.utils.BarUtils

/*
* BaseUIViewMode   UI 管理
* BaseDataViewModel  业务数据管理 可以未null
* */
abstract class MvvmBaseActivity <V : ViewDataBinding, UVM : BaseUIViewModel,DVM:BaseDataViewModel?> :
    CommonBaseActivity {

    private var dataVM: DVM? = null//dataViewModel
    private var UIVM: UVM? = null//UIViewModel
    private var viewDataBinding: V? = null//绑定布局layout的操作binding

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
        if (bindingVariable > 0 && UIVM != null) {
            viewDataBinding?.setVariable(bindingVariable, UIVM)
        } else {
        }
        viewDataBinding?.setLifecycleOwner(this)
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
    /*
    *暴露出去的binding 给activity 使用
    *  */
    fun getBinding() : V{
        return viewDataBinding!!
    }
    fun getUIVM():UVM{
        return UIVM!!
    }
    fun getDataVM():DVM{
        return dataVM!!
    }
}