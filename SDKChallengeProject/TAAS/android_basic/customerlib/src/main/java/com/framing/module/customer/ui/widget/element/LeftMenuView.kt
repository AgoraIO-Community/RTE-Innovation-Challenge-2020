package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
<<<<<<< HEAD
import com.framing.baselib.TLog
import com.framing.commonlib.base.IBindingViewClick
import com.framing.commonlib.base.IViewBindingClick
import com.framing.commonlib.widget.BaseObserverView
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
import com.framing.module.customer.R
import com.framing.module.customer.databinding.ContentLeftLayoutBinding

/*
 * Des  主页面左侧menu 控件元素
 * Author Young
 * Date 
<<<<<<< HEAD
 */class LeftMenuView : BaseObserverView {
=======
 */class LeftMenuView :FrameLayout{
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
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
<<<<<<< HEAD
    private  var click:IViewBindingClick?=null
=======

>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5

    private fun initView(){
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.content_left_layout,this,true);
    }
<<<<<<< HEAD
    fun progress(pro:Float){
        binding.motionLayout.progress=pro
    }
    fun clickEvent(click: IViewBindingClick){
        this.click=click
        binding.oneTv.setOnClickListener{
            TLog.log("test_id","${binding.oneTv.id}")
            click.onClick(this)
        }
        binding.twoTv.setOnClickListener{
            click.onClick(this)
        }
    }
=======
    fun showHide(isShow:Boolean){
    }
    fun progress(pro:Float){
        binding.motionLayout.progress=pro
    }
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
}