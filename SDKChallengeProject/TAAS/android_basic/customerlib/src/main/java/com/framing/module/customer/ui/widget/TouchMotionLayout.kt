package com.framing.module.customer.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout
import com.framing.baselib.TLog

/*
 * Des  
 * Author Young
 * Date 
 */class TouchMotionLayout : MotionLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    private var isLock=true//锁定map

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        TLog.log("motion_layout_dispatchTouchEvent","$isLock ${ev?.action}  ___ ${super.dispatchTouchEvent(ev)}")
        return isLock
    }
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        TLog.log("motion_layout_onInterceptTouchEvent","${event?.action}")
        return super.onInterceptTouchEvent(event)
    }
    fun setLock(isLoc:Boolean){
        isLock=isLoc
    }
    fun lock():Boolean{
        return isLock
    }
}