package com.framing.module.customer.ui.widget.element

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.framing.baselib.TLog
import com.framing.commonlib.base.IBindingClickEvent
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
    /*-------start--------
    * data 业务数据
    * btn  添加的独立UI
    * */
    private var data:DialogBean?=null
    private var btn:Button?=null


    override fun initView(attrs: AttributeSet?) {
        super.initView(attrs)
        binding?.titleTv?.run {
            this.gravity=Gravity.CENTER
        }
        addViewLogic()
    }

    @SuppressLint("ResourceAsColor")
    private fun addViewLogic(){
        val param= LayoutParams(DisplayUtils.dp2px(160f), DisplayUtils.dp2px(60f))
        param.gravity=Gravity.BOTTOM
        param.gravity=Gravity.CENTER_HORIZONTAL
        btn=Button(context)
        btn?.alpha=0.4f
        btn?.text="go"
        btn?.gravity=Gravity.CENTER
        btn?.setBackgroundColor(R.color.color_menu_science)
        addView(btn,childCount,param)
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
        dataToUi()
    }
    private fun styleAni(progress:Float){
        if(progress==1f){
            binding?.constraintLayout?.run {
                data?.run {
                    when(dialogLevel){
                        1->{
                            setTransition(R.id.to_warning)
                            binding?.titleTv?.text="Warning"
                            transitionToEnd()
                        }
                        2->{
                            setTransition(R.id.to_attention)
                            binding?.titleTv?.text="Attention"
                            transitionToEnd()
                        }
                        3->{
                            binding?.titleTv?.text="Foucs"
                        }
                    }
                }
                dataToUi()
            }
        }
    }

    private fun dataToUi() {
        data?.run {
            binding?.contentView?.contentBinding()?.run {
                if(subtitle.isEmpty()){
                    leftTipTv.visibility= View.GONE
                }else{
                    leftTipTv.visibility= View.VISIBLE
                    leftTipTv.text=subtitle
                }
                if(content.isEmpty()){
                    centerTv.visibility= View.GONE
                }else{
                    centerTv.visibility= View.VISIBLE
                    leftTipTv.text=content
                }
            }
        }
    }

    override fun  clickLisen(click: IBindingClickEvent<Any>) {
        super.clickLisen(click)
        btn?.setOnClickListener {
            click.onClick(it,data!!,0)
        }
    }
}