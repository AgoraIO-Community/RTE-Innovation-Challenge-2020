package com.framing.module.customer.ui.fragment

import android.os.Build
import android.os.Bundle
<<<<<<< HEAD
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionHelper
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.framing.baselib.TLog
import com.framing.commonlib.base.IBindingClickEvent
import com.framing.commonlib.base.IViewBindingClick
=======
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.lifecycle.lifecycleScope
import com.amap.api.maps.MapView
import com.framing.baselib.TLog
import com.framing.commonlib.base.IViewBindingClick
import com.framing.commonlib.helper.ObserVerScope
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.customer.databinding.MapFragmentLayoutBinding
import com.framing.module.customer.ui.viewmodel.MapUIVM
import com.framing.module.customer.data.viewmodel.MapDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.R
import com.framing.module.customer.data.viewmodel.CContainerDataVM
import com.framing.module.customer.ui.viewmodel.CContainerUIVM
<<<<<<< HEAD
import com.framing.module.customer.ui.widget.element.DataPercentView
import com.framing.module.customer.ui.widget.map.CustomerMapView
import com.framing.module.customer.ui.widget.map.MapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
=======
import com.framing.module.customer.ui.widget.map.CustomerMapView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5

/**
 * Des 进入首页map 地图
 * Created by VULCAN on 2020-08-13 13:30:55
 */
class  MapFragment :  MvvmBaseFragment<MapFragmentLayoutBinding, MapUIVM,MapDataVM>(),
<<<<<<< HEAD
    IViewBindingClick ,IBindingClickEvent<Any>{


    private var containerVM:CContainerDataVM?=null//activity datavm (相当于application)
    private var containerUIVM:CContainerUIVM?=null// activity UIvm (相当于application)
=======
    IViewBindingClick {


    private val mapView:CustomerMapView?=null
    private var isShow=false
    private var containerVM:CContainerDataVM?=null//activity datavm
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TLog.log("SimpleMapView","onViewCreated")
        getBinding().onClick= this
        containerVM=getActivityViewModelProvider(requireActivity()).get(CContainerDataVM::class.java)
<<<<<<< HEAD
        containerUIVM=getActivityViewModelProvider(requireActivity()).get(CContainerUIVM::class.java)
        getBinding().mapView.onCreate(savedInstanceState)
        lifecycle.addObserver(getBinding().mapView)
        lifecycle.addObserver(getBinding().bottomLayout)
        viewLogic()
=======
        getBinding().mapView.onCreate(savedInstanceState)
        lifecycle.addObserver(getBinding().mapView)
        viewLogic()
//        getBinding().mapLockImg.setOnClickListener {
//            TLog.log("map_onclick","${view.id}")
//        }
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
    }
    /*
    * UI
    * */
    private fun viewLogic() {
        initUI()
        initData()
    }

    /*
    * motionLayout 动效联动配置
    * 拿到datavm 配置给UI
    * 弹窗  data
    * leftmenu data
    * bottom data
    * right tip data
    * */
    private fun initData() {
<<<<<<< HEAD
        //dialog 点击调度
        containerUIVM?.isGoClick?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it){
                getBinding()?.contentContainer?.toHalf()
            }
        })
        //bottom view 观察调度
        containerVM?.bottomData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            getBinding()?.bottomLayout?. setBottomData(it!!)
        })
        //right tip science 数据调度
        containerVM?.scienceData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            getUIViewModel().loadScienceTip(it)
        })
        //right tip science 数据调度
        containerVM?.focusData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            getUIViewModel().loadFocusTip(it)
        })
        //right tip weather 数据调度
        containerVM?.weatherData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            getUIViewModel().loadWeatherTip(it)
        })
        //本业务调度给container
        getUIViewModel().isShowDialog.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            containerVM?.isShowDialog?.postValue(it)
        })
        //获得天气数据 调度给container 相当于app级别天气数据
        MapUtils.queryWeather("文水",requireContext()){
            TLog.log("weather_query","$it")
            containerVM?.weatherData?.postValue(it)
        }
=======
        containerVM?.bottomData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

        })
        getUIViewModel().isShowDialog.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            containerVM?.isShowDialog?.postValue(it)
        })
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
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
<<<<<<< HEAD
                TLog.log("motionLayout",
                    "onTransitionChange$p1 p2 $p2 p3 $p3 " +
                            "start---${R.id.start}"+
                            "dialog -- ${R.id.to_dialog}"+
                            " nameleft ${R.id.left_end}--bottom${R.id.bottom_end}" +
                            "one--${R.id.right_one_end} two${R.id.right_two_end} three${R.id.right_three_end}")
=======
                TLog.log("motionLayout","onTransitionChange$p1 p2 $p2 p3 $p3  name ${p0?.currentState} ")
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
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
<<<<<<< HEAD
                TLog.log("motionLayout","onTransitionCompleted"+p1+"___${R.id.right_one_end}")
                getBinding().run {
                    when(p1){
                        R.id.right_three_end->{
                            rightThree.complete(true)
                        }
                        R.id.right_two_end->{
                            rightTwo.complete(true)
                        }
                        R.id.right_one_end->{
                            rightOne.complete(true)
                        }
                    }
                }
            }
        })
    }
    private fun changeData(){

    }
    private fun initUI() {
        //地图初始配置
        getBinding().mapView.initLoad()
=======
                TLog.log("motionLayout","onTransitionCompleted"+p1+"___${R.id.right_three_end}")
                getBinding().rightThree.complete(p1==R.id.right_three_end)
                getBinding().rightTwo.complete(p1==R.id.right_two_end)
                getBinding().rightOne.complete(p1==R.id.right_one_end)
            }
        })
    }

    private fun initUI() {
        //地图初始配置
        getBinding().mapView.initLoad()
    }

    override fun onResume() {
        super.onResume()
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
        lifecycleScope.launchWhenResumed {
            withContext(Dispatchers.IO){
                Thread.sleep(10000)
                withContext(Dispatchers.Main){
<<<<<<< HEAD
                    TLog.log("dialog_postmap234","111")
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
                    getUIViewModel().isShowDialog.postValue(true)
                }
            }
        }
<<<<<<< HEAD
        //设置回收自定义view的点击事件
        getBinding()?.run {
            mapView?.onclick(this@MapFragment)
            leftLayout.clickEvent(this@MapFragment)
            rightOne.clickLisen(this@MapFragment)
            rightThree.clickLisen(this@MapFragment)
            bottomLayout?.clickEvent(this@MapFragment)
        }
    }

    override fun onResume() {
        super.onResume()
    }
    /*
    * onClick
    * onClick
    * 页面全部点击处理
    * 管理自定义view点击
    * */
    override fun onClick(view: View) {
        TLog.log("map_onclick","${view.id}---${getBinding().motionLayout.lock()}")
        when(view.id){
            R.id.map_lock_img->{
                if(getBinding().motionLayout.lock()){
                    getBinding().motionLayout.setLock(false)
                    getBinding().mapLockImg.setImageResource(R.mipmap.map_unlock_icon)
                }else{
                    getBinding().mapView.toCenterCam()
                    getBinding().motionLayout.setLock(true)
                    getBinding().mapLockImg.setImageResource(R.mipmap.map_lock)
                }
            }
            R.id.left_layout->{
                nav()?.navigate(R.id.action_mapFragment_to_userSettingFragment)
            }
            R.id.percen_one->{
                nav()?.navigate(R.id.action_mapFragment_to_materialFragment)
            }
            R.id.title_right_view->{
                nav()?.navigate(R.id.action_mapFragment_to_agroaFragment)
            }
        }
    }
    override fun onClick(view: View, data: Any, position: Int) {
        TLog.log("map_onclick",view.toString())
        when(view.id){
            R.id.map_view->{
                getBinding()?.contentContainer?.toHalf()
            }
            R.id.percent_view->{
                nav()?.navigate(R.id.action_mapFragment_to_materialFragment)
            }
=======
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
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
        }
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


<<<<<<< HEAD

=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
}