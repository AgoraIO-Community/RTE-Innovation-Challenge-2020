package com.framing.module.customer.ui.widget.element

import android.bluetooth.BluetoothAssignedNumbers.PARROT
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.framing.module.customer.ui.widget.support.CylinderView
import com.framing.module.customer.ui.widget.support.ParrotViewNew
import com.framing.module.customer.ui.widget.support.utils.ParrotPillarNew
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
    private val citys = arrayOf("California", "Texas", "Florida", "New York", "llinos", "Georgia", "Michigan", "New Jersey", "Pennsylvania", "Virginana", "Obhio", "U.S. Virgin Islands", "North Arolina", "South Carolina", "Maryland", "Colorado", "Minnersota", "Arizona", "Northern Marianas", "tokey")
    private val value1 = floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f, 17f, 18f, 19f, 20f)


    /*
    *
    * */
    private fun viewType(type:ViewType){
        val param=ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        if(type==ViewType.CYLINDER){
            cylView=CylinderView(context)
            cylView?.layoutParams=param
            addView(cylView)
            dataCyl()
        }else if(type==ViewType.PARROT){
            parView=ParrotViewNew(context)
            parView?.layoutParams=param
            addView(parView)
            dataPar()
        }
    }
    private fun dataCyl(){

    }
    private fun dataPar(){
        parView?.run {
            getFakeData()
            setColor(Color.parseColor("#cc8400"), Color.parseColor("#ffff00"))
            setData(mParrotPillars1,ParrotViewNew.ANIM_TYPE_COLECT)
        }
    }
    private fun getFakeData() {
        mParrotPillars1 = ArrayList()
        for (i in citys.indices) {
            mParrotPillars1!!.add(ParrotPillarNew(citys[i], value1[i]))
        }
    }

    enum class ViewType{
        CYLINDER,
        PARROT
    }
}