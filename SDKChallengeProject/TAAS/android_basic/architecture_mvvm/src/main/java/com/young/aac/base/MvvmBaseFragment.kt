package com.young.aac.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

/*
* BaseUIViewMode  UI 管理
* BaseDataViewModel 业务数据管理 可以未null
* */
abstract class MvvmBaseFragment  <V : ViewDataBinding, UVM : BaseUIViewModel,DVM:BaseDataViewModel?> :
    Fragment {

    constructor() : super()

    private var dataVM: DVM? = null//dataViewModel
    private var UIVM: UVM? = null//UIViewModel
    protected var mActivity: AppCompatActivity? = null
    private var viewDataBinding: V? = null
    private var mFragmentLogTag = ""


    abstract val bindingVariable: Int//BR id 绑定layout 和UIViewmode
    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun getUIViewModel(): UVM//页面UI管理 可以和宿主activity 统计fragment 共享
    abstract fun getDataViewModel(): DVM?//dataViewModel 业务数据


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return viewDataBinding?.getRoot()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        UIVM = getUIViewModel()
        dataVM=getDataViewModel()
        if (bindingVariable > 0 && UIVM != null) {
            viewDataBinding?.setVariable(bindingVariable, UIVM)
        } else {
        }
        viewDataBinding?.lifecycleOwner = this
    }

    //拿到app级别的共享VM
    protected open fun getAppViewModelProvider(): ViewModelProvider? {
        return (mActivity?.getApplicationContext() as MvvmBaseApplication).getAppViewModelProvider(mActivity!!)
    }

    //可以拿到 自己的ViewModel 级别 自己独有
    protected open fun getFragmentViewModelProvider(fragment: Fragment): ViewModelProvider {
        return ViewModelProvider(fragment, fragment.defaultViewModelProviderFactory)
    }

    //可以顺利的拿到 activity viewmode 可以和activity 或者同级fragment共享
    protected open fun getActivityViewModelProvider(activity: FragmentActivity): ViewModelProvider{
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }
    fun getBinding() : V{
        return viewDataBinding!!
    }
}