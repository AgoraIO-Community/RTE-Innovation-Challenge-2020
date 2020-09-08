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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.framing.baselib.TLog
import com.framing.commonlib.base.CommonBaseFragment

/*
* BaseUIViewMode  UI 管理
* BaseDataViewModel 业务数据管理 可以未null
* */
abstract class MvvmBaseFragment  <V : ViewDataBinding, UVM : BaseUIViewModel<V>,DVM:BaseDataViewModel?> :
    CommonBaseFragment {

    constructor() : super()

    private var dataVM: DVM? = null//dataViewModel
    private var UIVM: UVM? = null//UIViewModel
    protected var mActivity: AppCompatActivity? = null
    private var viewDataBinding: V? = null
    private var mFragmentLogTag = ""
    private var isInit=true//第一次

    abstract val bindingVariable: Int//BR id 绑定layout 和UIViewmode
    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun getUIViewModel(): UVM//页面UI管理  共享
    abstract fun getDataViewModel(): DVM?//dataViewModel 业务数据


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(isInit) {
            viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        }
        return viewDataBinding?.getRoot()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        TLog.log("isInit",isInit.toString())
        if(isInit) {
            UIVM = getUIViewModel()
            dataVM = getDataViewModel()
            UIVM?.withBinding(viewDataBinding!!, this)
            if (bindingVariable > 0 && UIVM != null) {
                viewDataBinding?.setVariable(bindingVariable, UIVM)
            } else {
            }
            viewDataBinding?.lifecycleOwner = this
            //转化data load 状态为UIloading
            getDataViewModel()?.loadType?.observe(viewLifecycleOwner, Observer {
                getUIViewModel()?.loadType.postValue(it)
            })
            isInit=false
        }
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
    protected open fun getBinding() : V{
        return viewDataBinding!!
    }
    /**
     * 为了给所有的fragment，导航跳转fragment的
     * @return
     */
    protected open fun nav(): NavController? {
        return NavHostFragment.findNavController(this)
    }
}