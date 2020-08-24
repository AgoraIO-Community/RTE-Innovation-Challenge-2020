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
        binding?.titleView?.run {
            this.gravity=Gravity.CENTER
        }
        addViewLogic()
    }
    private var mParrotPillars1: ArrayList<ParrotPillarNew>? = null
    private val citys = arrayOf("California", "Texas", "Florida", "New York", "llinos", "Georgia", "Michigan", "New Jersey", "Pennsylvania", "Virginana", "Obhio", "U.S. Virgin Islands", "North Arolina", "South Carolina", "Maryland", "Colorado", "Minnersota", "Arizona", "Northern Marianas", "tokey")

    private val value1 = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f, 17f, 18f, 19f, 20f)
    private fun getFakeData() {
        mParrotPillars1 = ArrayList()
        for (i in citys.indices) {
            mParrotPillars1!!.add(ParrotPillarNew(citys[i], value1[i]))
        }
    }
    private fun addViewLogic(){
        val params=LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(0, DisplayUtils.dp2px(40f), 0, 0)
        val addContent=LayoutInflater.from(context).inflate(
            R.layout.content_style_parent_layout, null
        )
        addContent.layoutParams=params
        addContent.alpha=0.9f
        val a=addContent.findViewById<ParrotViewNew>(R.id.percent_view)
        this.addView(addContent)
        addContent.setOnClickListener {
            TLog.log("ScienceDialog","test_123456")
        }
        GlobalScope.launch {
            Thread.sleep(1000)
            getFakeData()
            a.setColor(Color.parseColor("#cc8400"), Color.parseColor("#ffff00"))
            a.setData(mParrotPillars1,ParrotViewNew.ANIM_TYPE_COLECT)
            withContext(Dispatchers.Main){
                a.startAnim()
            }
        }
    }
    /*
   * 根据外层执行进度
   * */
    override fun progress(progress:Float){
        TLog.log("science_dialog","$progress")
        styleAni(progress)
    }
    private fun styleAni(progress:Float){
        if(progress==1f){
            binding?.constraintLayout?.run {
                setTransition(R.id.to_warning)
//                setTransition(R.id.to_attention)
                transitionToEnd()
            }
        }
    }
}