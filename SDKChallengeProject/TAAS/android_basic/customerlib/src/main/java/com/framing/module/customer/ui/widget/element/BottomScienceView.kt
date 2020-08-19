package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.framing.module.customer.R
import com.framing.module.customer.databinding.ContentBottomMainLayoutBinding

/*
 * Des  主页 底部滑动 科技
 * Author Young
 * Date 
 */class BottomScienceView : FrameLayout{
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

    private var binding:ContentBottomMainLayoutBinding?=null

    private fun initView(){
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.content_bottom_main_layout,this,true);
    }
    fun progress(progress:Float){
        binding?.motionLayout?.progress=progress
    }
}