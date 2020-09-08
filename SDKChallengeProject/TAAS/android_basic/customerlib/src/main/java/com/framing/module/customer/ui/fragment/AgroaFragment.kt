package com.framing.module.customer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.framing.baselib.TLog
import com.framing.commonlib.agora.AgoraLiviView
import com.framing.commonlib.base.IBindingClick
import com.framing.commonlib.base.IViewBindingClick
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.customer.databinding.FragmentAgroaLayoutBinding
import com.framing.module.customer.ui.viewmodel.AgroaUIVM
import com.framing.module.customer.data.viewmodel.AgroaDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.R
import com.framing.module.customer.data.RecordBean
import com.framing.module.customer.ui.adapter.RecordListAdapter
import com.framing.module.customer.ui.viewmodel.CContainerUIVM
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler

/**
 * Des live show
 * Created by VULCAN on 2020-09-05 23:32:15
 */
class AgroaFragment :  MvvmBaseFragment<FragmentAgroaLayoutBinding, AgroaUIVM,AgroaDataVM>(),
    IViewBindingClick,AgoraLiviView.IRtcStats,IBindingClick<Any>{
    private var containerUIVM:CContainerUIVM?=null// activity UIvm (相当于application)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().click=this
        containerUIVM=getActivityViewModelProvider(requireActivity()).get(CContainerUIVM::class.java)
        containerUIVM?.isConainerBgShow?.value=true
        initView()
        getDataViewModel()?.requestData()
    }
    /*
    * 初始化list的适配器 （bindingmethod 加载符合思想）
    * 观察reocorddata
    * */
    private fun initView() {
        val layoutManager= LinearLayoutManager(context)
        layoutManager?.orientation= LinearLayoutManager.HORIZONTAL
        val adapter= RecordListAdapter(requireContext(),this)
        getBinding()?.recordList?.layoutManager=layoutManager
        getBinding()?.recordList?.adapter=adapter
        getBinding()?.run {
            attetionView?.repeatCount=3
            liveView?.startPrepare(Constants.CLIENT_ROLE_AUDIENCE,this@AgroaFragment)
            constraintLayout5.addTransitionListener(object : MotionLayout.TransitionListener{
                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                }
                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }
                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                }
                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    when(p1){
                        R.id.top_end->{
                            liveView?.onDestory()
                            medialView?.stop()
                            containerUIVM?.isConainerBgShow?.value=false
                            nav()?.navigateUp()
                        }
                    }
                }
            })
        }
        getDataViewModel()?.recordDatas?.observe(viewLifecycleOwner, Observer {
            adapter.dataList=it
        })
    }
    /*
    * onClick xml 添加不能有多个参数的形式 对应不同业务点击需要多个回调
    * onClick
    * statusResult 直播状态对上层表现处理
    * */
    override fun onClick(view: View) {
        when(view.id){
            R.id.data_view->{
                getBinding()?.liveView?.onDestory()
                getBinding()?.medialView?.pause()
                nav()?.navigate(R.id.action_agroaFragment_to_materialFragment)
            }
        }
    }
    override fun onClick(data: Any) {
        getBinding()?.medialView?.run {
            val data=data as RecordBean
            if(!isPlaying()){
                visibility=View.VISIBLE
                startWith(data.url)
            }
        }
    }
    override fun statusResult(stats: IRtcEngineEventHandler.RtcStats?) {
        TLog.log("statusResult","stats")
        getBinding()?.run {
            if(stats?.users==1&&liveView?.visibility== View.VISIBLE){
                requireActivity().runOnUiThread {
                    liveView?.visibility= View.GONE
                    recordList?.visibility= View.VISIBLE
                }
            }
        }
    }


    /*
    * 基础支持
    * */
    override fun getUIViewModel(): AgroaUIVM {
        return getActivityViewModelProvider(requireActivity()).get(AgroaUIVM::class.java)
    }
    override fun getDataViewModel(): AgroaDataVM? {
        return getActivityViewModelProvider(requireActivity()).get(AgroaDataVM::class.java)!!
    }
    override val bindingVariable: Int
        get() = BR.agroaUIVM
    override val layoutId: Int
        get() = R.layout.fragment_agroa_layout


}