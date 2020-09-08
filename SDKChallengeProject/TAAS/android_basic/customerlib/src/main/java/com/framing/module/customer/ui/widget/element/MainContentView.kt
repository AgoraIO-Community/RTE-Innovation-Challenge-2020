package com.framing.module.customer.ui.widget.element

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.framing.baselib.TLog
import com.framing.commonlib.agora.AgoraLiviView
import com.framing.commonlib.base.IBindingClick
import com.framing.commonlib.utils.ScreenUtils
import com.framing.module.customer.R
import com.framing.module.customer.data.RecordBean
import com.framing.module.customer.databinding.MapCenterContentLayoutBinding
import com.framing.module.customer.ui.adapter.RecordListAdapter
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler


/*
 * Des map page top live view
 * 不做视频播放 切换到Agroafragment（attention）
 * Author Young
 * Date 
 */class MainContentView :FrameLayout,IBindingClick<Any>,AgoraLiviView.IRtcStats{

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context) : super(context){
        initView()
    }
    private var binding:MapCenterContentLayoutBinding?=null
    private var params: ViewGroup.LayoutParams?=null
    private var downX:Float=0f//手势处理记录按下x
    private var downY:Float=0f//手势处理记录按下Y
    private var targetOffset=0f//阈值
    private var layoutManager:LinearLayoutManager?=null
    private var adapter:RecordListAdapter?=null


    private fun initView() {
        binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.map_center_content_layout,this,true);
        isClickable=true
        layoutManager=LinearLayoutManager(context)
        layoutManager?.orientation=LinearLayoutManager.HORIZONTAL
        adapter= RecordListAdapter(context,this)
        binding?.recordList?.layoutManager=layoutManager
        binding?.recordList?.adapter=adapter
    }
    fun setRecordData(datas:List<RecordBean>){
        adapter?.dataList=datas
        if(datas?.size>0){
            binding?.recordList?.visibility= View.VISIBLE
        }
    }
    @SuppressLint("ResourceAsColor")
    fun toHalf(){
        TLog.log("test_tohalf","topcontainer")
        visibility= View.VISIBLE
        params= layoutParams
        params?.width=ScreenUtils.getScreenWidth()
        params?.height=ScreenUtils.getScreenHeight()/2
        layoutParams=params
        params=binding?.liveView?.layoutParams
        params?.width=ScreenUtils.getScreenWidth()
        params?.height=ScreenUtils.getScreenHeight()/2
        binding?.containerView?.layoutParams=params
        beLive()
    }
    fun toAll(){
        visibility= View.VISIBLE
        params= layoutParams
        params?.width=ScreenUtils.getScreenWidth()
        params?.height=ScreenUtils.getScreenHeight()
        layoutParams=params
        params=binding?.liveView?.layoutParams
        params?.width=ScreenUtils.getScreenWidth()
        params?.height=ScreenUtils.getScreenHeight()
        binding?.containerView?.layoutParams=params
        beLive()
    }
    fun miss(){
        visibility= View.GONE
        params= layoutParams
        params?.width=ScreenUtils.getScreenWidth()
        params?.height=0
        layoutParams=params
        params=binding?.liveView?.layoutParams
        params?.height=0
        binding?.containerView?.layoutParams=params
        binding?.liveView?.onDestory()
        binding?.medialView?.stop()
    }
    fun beLive(){
        binding?.liveView?.startPrepare(Constants.CLIENT_ROLE_AUDIENCE,this)
    }

    /*
    * 来点手势
    * 上滑小下滑大
    * */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        TLog.log("onTouch_main_content","${event?.action}")
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                downX=event?.x
                downY=event?.y
            }
            MotionEvent.ACTION_UP->{
               targetOffset= event.y-downY
                TLog.log("onTouch_main_content_ACTION_UP","${targetOffset}---${(targetOffset<50f)}")
                if(targetOffset<-100f){//100f作为上滑阈值
                    miss()
                }
            }
            MotionEvent.ACTION_MOVE->{
                TLog.log("ACTION_MOVE","${event.y}")
                if(downY>event.y) {
                    changeSize((downY - event.y).toInt())
                }
            }
        }
        return super.onTouchEvent(event)
    }
    private fun changeSize(changeHeight: Int){
        params= layoutParams
        params?.height= params?.height?.minus(changeHeight)
        layoutParams=params
        params=binding?.liveView?.layoutParams
        params?.height= params?.height?.minus(changeHeight)
        binding?.liveView?.layoutParams=params
    }

    override fun onClick(data: Any) {//列表点击
//        binding?.run {
//            TLog.log("on_click","12345${medialView.isPlaying()}")
//            if(!medialView.isPlaying()) {
//                medialView.visibility = View.VISIBLE
//                medialView?.startWith(data.url)
//            }
//        }
    }

    override fun statusResult(stats: IRtcEngineEventHandler.RtcStats?) {
        if(stats?.users==1){
//            binding?.liveView?.alpha=0f
//            binding?.recordList?.visibility= View.VISIBLE
        }
    }
}

