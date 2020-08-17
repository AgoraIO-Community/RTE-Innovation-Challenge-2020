package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.framing.module.customer.R
import com.framing.module.customer.databinding.ContentLeftLayoutBinding

/*
 * Des  主页面左侧menu 控件元素
 * Author Young
 * Date 
 */class LeftMenuView :FrameLayout{
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

    private lateinit var binding:ContentLeftLayoutBinding


    private fun initView(){
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.content_left_layout,this,true);
    }
    fun showHide(isShow:Boolean){
        if(isShow) {
            binding.motionLayout.transitionToState(R.id.content_left_transition)
            binding.motionLayout.transitionToStart()
            binding.motionLayout.transitionToEnd()
        }else{
            binding.motionLayout.transitionToState(R.id.content_left_transition)
            binding.motionLayout.transitionToEnd()
            binding.motionLayout.transitionToStart()
        }
    }
}