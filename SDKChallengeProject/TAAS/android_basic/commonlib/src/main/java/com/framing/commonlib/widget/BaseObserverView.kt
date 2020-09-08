package com.framing.commonlib.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.framing.baselib.TLog
import kotlinx.coroutines.*

/*
 * Des  
 * Author Young
 * Date 
 */abstract  class BaseObserverView :FrameLayout ,LifecycleObserver {
    constructor(context: Context) : super(context){
    }
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

    val viewScope= GlobalScope
    var job:Job?=null
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destory(){
        try {
            job?.cancel()
            viewScope.cancel()
        }catch (e:IllegalStateException){
            TLog.log("scope does not have a job ")
        }
    }
}