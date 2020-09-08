package com.framing.module.customer.ui.widget.element

import android.bluetooth.BluetoothAssignedNumbers.PARROT
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.framing.baselib.TLog
import com.framing.module.customer.ui.widget.support.CylinderView
import com.framing.module.customer.ui.widget.support.ParrotViewNew
import com.framing.module.customer.ui.widget.support.utils.ParrotPillarNew
import java.lang.reflect.Array
import java.util.ArrayList

/*
 * Des  
 * Author Young
 * Date 
 */class DataPercentView : FrameLayout{
    constructor(context: Context) : super(context){

    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){

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

    private var cylView:CylinderView?=null
    private var parView:ParrotViewNew?=null
    private var mParrotPillars1: ArrayList<ParrotPillarNew>? = null
    var textList = arrayOf("氮","磷","钾","有机质","酸碱","硼","钼","铜","铁","锰","锌","钙","镁")
    private var percentList = floatArrayOf(120f,100f,80f,70f,70f,60f,50f,40f,30f,25f,20f,15f,10f)
    /*
    * 钾氮磷 ph 1.3:1∶0.4:0.3 有效比
    * */
    private var clvList = arrayListOf(
        CylinderView.Entry(156f,  Color.parseColor("#802979ff")),
        CylinderView.Entry(120f,  Color.parseColor("#80bbdefb")),
        CylinderView.Entry(48f,  Color.parseColor("#802979ff")),
        CylinderView.Entry(36f,  Color.parseColor("#8090caf9")))

    fun viewType(type:ViewType){
        val param=ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        if(type==ViewType.CYLINDER){
            cylView=CylinderView(context)
            cylView?.layoutParams=param
            addView(cylView)
            dataCyl()
            cylView?.setAttentionText("钾、氮、磷、ph 1.3:1∶0.4:0.3 有效比")
            cylView?.startSingleAnim()
        }else if(type==ViewType.PARROT){
            parView=ParrotViewNew(context)
            parView?.layoutParams=param
            addView(parView)
            dataPar()
            parView?.startAnim()
        }
    }
    fun doAniAll(){
        parView?.startAnim()
        cylView?.startUpAllAnim()
    }
    fun setDataPar(list:ArrayList<String>){
        if(list.size==textList.size){
            for((i,e) in list.withIndex()){

                if(i!=3) {
                    textList[i]+=e + "mg"
                }else{
                    textList[3]+=e + "g"
                }
                TLog.log("bottombean","${e + "mg"} __ ${textList[i]}")
            }
        }
        TLog.log("bottombean","${list.size==textList.size} __ ${textList[0]}")

    }
    private fun dataCyl(){
        cylView?.run {
            setData(clvList)
        }
    }
    private fun dataPar(){
        parView?.run {
            getFakeData()
            setColor(Color.parseColor("#cc8400"), Color.parseColor("#ffff00"))
            setData(mParrotPillars1,ParrotViewNew.ANIM_TYPE_COLECT)
            parView?.startAnim()
        }
    }
    private fun getFakeData() {
        mParrotPillars1 = ArrayList()
        for (i in textList.indices) {
            mParrotPillars1!!.add(ParrotPillarNew(textList[i], percentList[i]))
        }
    }

    enum class ViewType{
        CYLINDER,
        PARROT
    }
}