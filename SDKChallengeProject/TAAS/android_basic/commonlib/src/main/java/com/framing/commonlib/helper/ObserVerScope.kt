package com.framing.commonlib.helper

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/*
 * Des  部分场景关闭协程闭塞
 * Author Young
 * Date 
 */class ObserVerScope : CoroutineScope,LifecycleObserver {
    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destory(){
        this.destory()
    }
}