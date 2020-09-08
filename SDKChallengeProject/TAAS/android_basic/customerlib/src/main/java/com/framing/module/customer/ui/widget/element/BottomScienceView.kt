package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.framing.baselib.TLog
import com.framing.commonlib.base.IViewBindingClick
import com.framing.commonlib.widget.BaseObserverView
import com.framing.module.customer.R
import com.framing.module.customer.databinding.ContentBottomMainLayoutBinding
import com.young.bean.BottomBean
import kotlinx.coroutines.*

/*
 * Des  主页 底部滑动 科技
 * Author Young
 * Date 
 */class BottomScienceView : BaseObserverView {
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
    //根据外部加载的进度初始化
    fun progress(progress:Float){
        binding?.motionLayout?.progress=progress
        TLog.log("process_","$progress")
        TLog.log("bottombean22","$progress")
        if(progress==1f){
            viewScope.launch {
                withContext(Dispatchers.Main){
//                    binding?.percenOne?.visibility= View.VISIBLE
                    binding?.descriptionTv?.visibility= View.VISIBLE
                    percentAction()
                }
            }
        }else if(progress<0.1f){
            binding?.percenOne?.visibility= View.GONE
            binding?.descriptionTv?.visibility= View.GONE
        }
    }
    //加载展开的动效
    fun percentAction(){
        binding?.run {
            percenOne?.doAniAll()
        }
    }
    //底部数据转换
    fun setBottomData(data:BottomBean){
        TLog.log("bottombean11","$data")
        viewScope.launch {
            withContext(Dispatchers.IO){
                var a=data.list[0].nutrientBean
                var nurList= arrayListOf(
                    a.nitrogen,a.phosphorus,a.potassium,
                    a.organic,a.ph,a.boron,a.mo,a.copper,
                    a.iron,a.manganese,a.zinc,a.calcium,a.manganese
                )
                binding?.percenOne?.setDataPar(nurList)
                withContext(Dispatchers.Main){
                    binding?.run {
                        percenOne?.viewType(DataPercentView.ViewType.PARROT)
                        titleRightView?.text=data.list[0].title
                        descriptionTv?.text=if(data.list[0].content.isEmpty()) "nothing" else data.list[0].content
                    }
                }
            }
        }
    }

    fun clickEvent(click: IViewBindingClick){
        binding?.percenOne?.setOnClickListener{
            click.onClick(it)
        }
        binding?.titleRightView?.setOnClickListener {
            click.onClick(it)
        }
    }

}