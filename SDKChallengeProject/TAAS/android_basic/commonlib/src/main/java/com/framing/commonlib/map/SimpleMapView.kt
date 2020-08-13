package com.framing.commonlib.map

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.framing.support.map.FramingMapView

/*
 * Des  common 封装类 同步上下文（activity/fragment）生命周期处理 MapView
 * Author Young
 * Date 
 */
class SimpleMapView : FramingMapView,LifecycleObserver{

    constructor(p0: Context?) : super(p0)
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1)
    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateLife(){
        onCreate(null)
        Log.i("SimpleMapView","oncreate")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseLife(){
        onPause()
        Log.i("SimpleMapView","onPauseLife")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestoryLife(){
        onDestroy()
        Log.i("SimpleMapView","onDestoryLife")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeLife(){
        onResume()
        Log.i("SimpleMapView","onResumeLife")
    }
}