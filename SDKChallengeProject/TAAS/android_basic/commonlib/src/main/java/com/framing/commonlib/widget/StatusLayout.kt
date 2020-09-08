package com.framing.commonlib.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.framing.baselib.TLog
import com.framing.commonlib.R
import com.framing.commonlib.databinding.BaseLoadingLayoutBinding
import kotlinx.coroutines.*

/*
 * Des  
 * Author Young
 * Date 
 */class StatusLayout : BaseObserverView{
    constructor(context: Context) : super(context){
        initView()
    }
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

    private var binding:BaseLoadingLayoutBinding?=null
    private var loadingSec=0//5秒
    private var attContent=""
    private var currentStatus=StatusType.HIDE
    private fun initView(){
        binding=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.base_loading_layout,this,true);
//        binding?.loadingView?.setOnClickListener {
//
//        }
    }
    fun attentionText(content:String){
        attContent=content
    }
    fun statusType(type:StatusType){
        when(type){
            StatusType.LOADING->{
                visibility= View.VISIBLE
                if(currentStatus!=StatusType.LOADING) {
                    currentStatus = StatusType.LOADING
                    loadingLogic()
                }
            }
            StatusType.NO_INTERNET->{
                currentStatus=StatusType.NO_INTERNET
                visibility= View.VISIBLE
                binding?.attentionTv?.text=resources.getText(R.string.app_loading_no_network)
                binding?.loadingAniView?.alpha=0.5f
                binding?.loadingAniView?.loop(false)
                binding?.loadingAniView?.setAnimation("no_internet.json")
                binding?.loadingAniView?.playAnimation()
                try {
                    job?.cancel()
                }catch (ex:IllegalStateException){
                    TLog.log("no_job_on_scope")
                }
            }
            StatusType.ERROR->{
                currentStatus=StatusType.ERROR
                visibility= View.VISIBLE
                binding?.attentionTv?.text=resources.getText(R.string.app_loading_error)
                binding?.loadingAniView?.alpha=1f
                binding?.loadingAniView?.loop(true)
                binding?.loadingAniView?.setAnimation("track_load_error.json")
                binding?.loadingAniView?.playAnimation()
                try {
                    job?.cancel()
                }catch (ex:IllegalStateException){
                    TLog.log("no_job_on_scope")
                }
            }
            StatusType.HIDE->{
                currentStatus=StatusType.HIDE
                visibility= View.GONE
                binding?.loadingAniView?.clearAnimation()
                try {
                    job?.cancel()
                }catch (ex:IllegalStateException){
                    TLog.log("no_job_on_scope")
                }
            }
            StatusType.ATTENTION->{
                try {
                    job?.cancel()
                }catch (ex:IllegalStateException){
                    TLog.log("no_job_on_scope")
                }
                visibility= View.VISIBLE
                if(currentStatus!=StatusType.ATTENTION) {
                    binding?.loadingAniView?.clearAnimation()
                    binding?.loadingAniView?.loop(true)
                    binding?.loadingAniView?.setAnimation("tractor_working.json")
                    binding?.loadingAniView?.playAnimation()
                }
                binding?.attentionTv?.text=attContent
                currentStatus=StatusType.ATTENTION
            }
            StatusType.EMPTY->{
                currentStatus=StatusType.EMPTY
                visibility= View.VISIBLE
                binding?.loadingAniView?.loop(false)
                binding?.loadingAniView?.setAnimation("empty_data.json")
                binding?.loadingAniView?.playAnimation()
                binding?.attentionTv?.text=resources.getText(R.string.app_loading_empty)
                try {
                    job?.cancel()
                }catch (ex:IllegalStateException){
                    TLog.log("no_job_on_scope")
                }
            }
        }
    }

    private fun loadingLogic() {
        loadingSec=0
        binding?.loadingAniView?.run {
            alpha=0.8f
            loop(true)
            setAnimation("tractor_working.json")
            playAnimation()
        }
        job=viewScope.launch {
            for (i in 6 downTo  1) {
                /*
            * 编排趣味loading文案
            * 农村拖拉机的神奇力量
            * 其实都是单缸机
            * */
                if(!isActive){
                    return@launch
                }
                withContext(Dispatchers.Main){
                    when (loadingSec) {
                        0 -> {
                            binding?.attentionTv?.text =
                                "单缸拖拉机努力${resources.getText(R.string.app_loading_tx)}"
                        }
                        1 -> {
                            binding?.attentionTv?.text =
                                "双缸拖拉机奋力${resources.getText(R.string.app_loading_tx)}"
                        }
                        2-> {
                            binding?.attentionTv?.text =
                                "叁缸拖拉机拼命${resources.getText(R.string.app_loading_tx)}"
                        }
                        3 -> {
                            binding?.attentionTv?.text =
                                "肆缸拖拉机喷火${resources.getText(R.string.app_loading_tx)}"
                        }
                        4 -> {
                            binding?.attentionTv?.text =
                                "伍缸拖拉机爆缸${resources.getText(R.string.app_loading_tx)}"
                        }
                        5->{
//                            statusType(StatusType.ERROR)
                            this.cancel()
                            job?.cancel()
                            loadingLogic()
                        }
                    }
                }
                if(loadingSec<5) {//循环加载
                    loadingSec++
                    Thread.sleep(1500)
                }else{
                    loadingSec=0
                }
            }
        }
    }
    enum class StatusType{
        LOADING,
        NO_INTERNET,
        ERROR,
        HIDE,
        EMPTY,
        ATTENTION
    }
}