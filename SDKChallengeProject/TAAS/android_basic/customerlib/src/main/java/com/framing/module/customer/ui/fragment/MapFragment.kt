package com.framing.module.customer.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import com.amap.api.maps.MapView
import com.framing.baselib.TLog
import com.framing.commonlib.base.IBindingClickA
import com.framing.commonlib.base.IViewBindingClick
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.customer.databinding.MapFragmentLayoutBinding
import com.framing.module.customer.ui.viewmodel.MapUIVM
import com.framing.module.customer.data.viewmodel.MapDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.R
import com.framing.module.customer.ui.viewmodel.CContainerUIVM
import com.framing.module.customer.ui.widget.map.CustomerMapView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Des 进入首页map 地图
 * Created by VULCAN on 2020-08-13 13:30:55
 */
class  MapFragment :  MvvmBaseFragment<MapFragmentLayoutBinding, MapUIVM,MapDataVM>(),
    IViewBindingClick ,IBindingClickA<Object>{


    private val mapView:CustomerMapView?=null
    private var isShow=false
    private var containerVM:CContainerUIVM?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().onClick= this
        getBinding().onClick2=this
        containerVM=getActivityViewModelProvider(requireActivity()).get(CContainerUIVM::class.java)
        getBinding().mapView.onCreate(savedInstanceState)
        viewLogic()
//        getBinding().mapLockImg.setOnClickListener {
//            TLog.log("map_onclick","${view.id}")
//        }
    }
    /*
    * UI
    * */
    private fun viewLogic() {
        //地图初始配置
        getBinding().mapView.initLoad()
        //添加xml parent motionlayout 监听
        getBinding().motionLayout.addTransitionListener(object :TransitionListener{
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                TLog.log("motionLayout","onTransitionTrigger")
            }
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                TLog.log("motionLayout","onTransitionStarted")
            }
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                TLog.log("motionLayout","onTransitionChange$p1 p2 $p2 p3 $p3  name ${p0?.currentState} ")
                if(p1==R.id.left_end||p2==R.id.left_end){//确定命中得transition
                    getBinding().leftLayout.progress(p3)
                }else if(p1==R.id.bottom_end||p2==R.id.bottom_end){
                    getBinding().bottomLayout.progress(p3)
                }else if(p1==R.id.right_one_end||p2==R.id.right_one_end){
                    getBinding().rightOne.progress(p3)
                }else if(p1==R.id.right_two_end||p2==R.id.right_two_end){
                    getBinding().rightTwo.progress(p3)
                }else if(p1==R.id.right_three_end||p2==R.id.right_three_end){
                    getBinding().rightThree.progress(p3)
                }
            }
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                TLog.log("motionLayout","onTransitionCompleted"+p1+"___${R.id.right_three_end}")
                getBinding().rightThree.complete(p1==R.id.right_three_end)
                getBinding().rightTwo.complete(p1==R.id.right_two_end)
                getBinding().rightOne.complete(p1==R.id.right_one_end)
            }
        })
        GlobalScope.launch {
            Thread.sleep(6000)
            containerVM?.isDialogShow?.postValue(true)//通知容器布局 显示弹窗
        }
    }

    override fun onResume() {
        super.onResume()
    }
    /*
    * 页面全部点击处理
    *
    * */
    override fun onClick(view: View) {
        TLog.log("map_onclick","${view.id}")
        when(view.id){
            R.id.map_lock_img->{
                if(getBinding().mapView.isLock()){
                    getBinding().mapLockImg.setImageResource(R.mipmap.map_lock)
                }else{
                    getBinding().mapLockImg.setImageResource(R.mipmap.map_unlock_icon)
                }
            }
        }
    }
    override fun onClick(view: View, data: Object, position: Int) {
        TLog.log("map_onclick2","${view.id}")
    }
    /*
    * 以下是初始封装得配置
    * getUIViewModel  控制UI得viewmodel
    * getDataViewModel 数据控制层
    * bindingVariable 绑定UIViewmodel 到xml
    * layoutId 布局id
    * */
    override fun getUIViewModel(): MapUIVM {
        return getFragmentViewModelProvider(this).get(MapUIVM::class.java)
    }
    override fun getDataViewModel(): MapDataVM? {
        return getFragmentViewModelProvider(this).get(MapDataVM::class.java)
//        return getActivityViewModelProvider(requireActivity()).get(MapDataVM::class.java)!!
    }
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.map_fragment_layout


}