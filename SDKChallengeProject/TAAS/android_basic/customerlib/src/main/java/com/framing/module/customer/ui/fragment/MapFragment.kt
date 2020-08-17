package com.framing.module.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import com.amap.api.maps.MapView
import com.framing.baselib.TLog
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.customer.databinding.MapFragmentLayoutBinding
import com.framing.module.customer.ui.viewmodel.MapUIVM
import com.framing.module.customer.data.viewmodel.MapDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.R
import com.framing.module.customer.ui.widget.map.CustomerMapView

/**
 * Des 进入首页map 地图
 * Created by VULCAN on 2020-08-13 13:30:55
 */
class  MapFragment :  MvvmBaseFragment<MapFragmentLayoutBinding, MapUIVM,MapDataVM>(){


    private val mapView:CustomerMapView?=null
    private var isShow=false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().mapView.onCreate(savedInstanceState)
        getBinding().mapView.initLoad()
//        lifecycle.addObserver(getBinding().mapView)
//        getBinding().mapView.initLoad()
         getBinding().motionLayout.addTransitionListener(object :TransitionListener{
             override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                    TLog.log("motionLayout","onTransitionTrigger")
             }
             override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                 TLog.log("motionLayout","onTransitionStarted")
             }

             override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                 TLog.log("motionLayout","onTransitionChange")
             }

             override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                 TLog.log("motionLayout","onTransitionCompleted"+p1)
                 isShow= if(isShow) false else true
                 getBinding().leftMenu.showHide(isShow)
             }
         })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun getUIViewModel(): MapUIVM {
        return getActivityViewModelProvider(requireActivity()).get(MapUIVM::class.java)
    }
    override fun getDataViewModel(): MapDataVM? {
        return getActivityViewModelProvider(requireActivity()).get(MapDataVM::class.java)!!
    }
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.map_fragment_layout
}