package com.framing.module.customer.ui.widget.element

import android.bluetooth.BluetoothAssignedNumbers.PARROT
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
<<<<<<< HEAD
import com.framing.baselib.TLog
import com.framing.module.customer.ui.widget.support.CylinderView
import com.framing.module.customer.ui.widget.support.ParrotViewNew
import com.framing.module.customer.ui.widget.support.utils.ParrotPillarNew
import java.lang.reflect.Array
=======
import com.framing.module.customer.ui.widget.support.CylinderView
import com.framing.module.customer.ui.widget.support.ParrotViewNew
import com.framing.module.customer.ui.widget.support.utils.ParrotPillarNew
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
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
<<<<<<< HEAD
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
=======
    private val citys = arrayOf("California", "Texas", "Florida", "New York", "llinos", "Georgia", "Michigan", "New Jersey", "Pennsylvania", "Virginana", "Obhio", "U.S. Virgin Islands", "North Arolina", "South Carolina", "Maryland", "Colorado", "Minnersota", "Arizona", "Northern Marianas", "tokey")
    private val value1 = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f, 17f, 18f, 19f, 20f)


    /*
    *
    * */
    private fun viewType(type:ViewType){
        val param=ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
        if(type==ViewType.CYLINDER){
            cylView=CylinderView(context)
            cylView?.layoutParams=param
            addView(cylView)
            dataCyl()
<<<<<<< HEAD
            cylView?.setAttentionText("钾、氮、磷、ph 1.3:1∶0.4:0.3 有效比")
            cylView?.startSingleAnim()
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
        }else if(type==ViewType.PARROT){
            parView=ParrotViewNew(context)
            parView?.layoutParams=param
            addView(parView)
            dataPar()
<<<<<<< HEAD
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
=======
        }
    }
    private fun dataCyl(){

>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
    }
    private fun dataPar(){
        parView?.run {
            getFakeData()
            setColor(Color.parseColor("#cc8400"), Color.parseColor("#ffff00"))
            setData(mParrotPillars1,ParrotViewNew.ANIM_TYPE_COLECT)
<<<<<<< HEAD
            parView?.startAnim()
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
        }
    }
    private fun getFakeData() {
        mParrotPillars1 = ArrayList()
<<<<<<< HEAD
        for (i in textList.indices) {
            mParrotPillars1!!.add(ParrotPillarNew(textList[i], percentList[i]))
=======
        for (i in citys.indices) {
            mParrotPillars1!!.add(ParrotPillarNew(citys[i], value1[i]))
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
        }
    }

    enum class ViewType{
        CYLINDER,
        PARROT
    }
}