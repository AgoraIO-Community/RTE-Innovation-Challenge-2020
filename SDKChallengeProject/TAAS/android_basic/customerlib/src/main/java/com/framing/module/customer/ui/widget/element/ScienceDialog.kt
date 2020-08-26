package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.framing.baselib.TLog
import com.framing.commonlib.utils.DisplayUtils
import com.framing.commonlib.utils.ScreenUtils
import com.framing.module.customer.R
import com.framing.module.customer.ui.widget.support.ParrotViewNew
import com.framing.module.customer.ui.widget.support.utils.ParrotPillarNew
import com.young.bean.DialogBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList


/*
 * Des  对于在containeractivity 中显示  这个view就是app级别的dialog
 * Author Young
 * Date 
 */class ScienceDialog : ScienceContentView{

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
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

    override fun initView(attrs: AttributeSet?) {
        super.initView(attrs)
        binding?.titleTv?.run {
            this.gravity=Gravity.CENTER
        }
        addViewLogic()
    }
    private var data:DialogBean?=null

    private fun addViewLogic(){
//        GlobalScope.launch {
//            Thread.sleep(5000)
//            getFakeData()
//            a.setColor(Color.parseColor("#cc8400"), Color.parseColor("#ffff00"))
//            a.setData(mParrotPillars1,ParrotViewNew.ANIM_TYPE_COLECT)
//            withContext(Dispatchers.Main){
//                a.startAnim()
//            }
//        }
    }
    /*
   * 根据外层执行进度
   * */
    override fun progress(progress:Float){
        TLog.log("science_dialog","$progress")
        styleAni(progress)
    }
    fun setData(data:DialogBean){
        this.data=data
    }
    private fun styleAni(progress:Float){
        if(progress==1f){
            binding?.constraintLayout?.run {
                setTransition(R.id.to_warning)
                binding?.titleTv?.text="Warning"
//                setTransition(R.id.to_attention)
                transitionToEnd()
                dataToUi()
            }
        }
    }

    private fun dataToUi() {
        data?.run {
            binding?.contentView?.contentBinding()?.run {
                leftTipTv.text=subtitle
                centerTv.text=content
            }
        }
    }
}