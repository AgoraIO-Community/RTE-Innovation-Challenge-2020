package com.framing.module.customer.ui.widget.map

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.framing.commonlib.utils.DisplayUtils
import com.framing.module.customer.R
import com.framing.module.customer.databinding.MapMakerLayoutBinding

/*
 * Des  
 * Author Young
 * Date 
 */class MarkerView :FrameLayout{

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
    private var binding:MapMakerLayoutBinding?=null

    private fun initView(){
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.map_maker_layout,this,true);
    }
    fun  viewType(type:MakerType){
        binding?.run {
            when(type){
                MakerType.UAV_PRO ->{
                    titleTv.text=resources.getText(R.string.app_customer_uav_pro)
                   aniView.setAnimation("uav_working.json")
                }
                MakerType.UAV_WATCH ->{
                    titleTv.text=resources.getText(R.string.app_customer_uav_watch)
                    aniView.setAnimation("uav_watch.json")
                }
                MakerType.Track_PRO ->{
                    titleTv.text=resources.getText(R.string.app_customer_track_pro)
                    aniView.setAnimation("tractor_working.json")
                }
            }
            aniView.playAnimation()
        }
    }
    enum class MakerType{
        UAV_PRO ,
        UAV_WATCH,
        Track_PRO
    }
}