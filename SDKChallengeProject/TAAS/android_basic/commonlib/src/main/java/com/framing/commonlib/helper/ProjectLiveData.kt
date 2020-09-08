package com.framing.commonlib.helper

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*


/*
 * Des 解决倒灌  没有影响的类 建议使用MutableLiveData
 * Author Young
 * Date 
 */class ProjectLiveData<T> : MutableLiveData<T> {
    constructor() : super()
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        /**
         * 不要执行 解决倒灌
         *         observer.mLastVersion = mVersion;
         *         observer.mObserver.onChanged((T) mData);
         */
        hook(observer)
    }

    private fun hook(observer: Observer<in T>) {
        val liveDataClass = LiveData::class.java
        try {
            //获取field private SafeIterableMap<Observer<T>, ObserverWrapper> mObservers
            val mObservers: Field = liveDataClass.getDeclaredField("mObservers")
            mObservers.setAccessible(true)
            //获取SafeIterableMap集合mObservers
            val observers: Any = mObservers.get(this)
            val observersClass: Class<*> = observers.javaClass
            //获取SafeIterableMap的get(Object obj)方法
            val methodGet: Method = observersClass.getDeclaredMethod("get", Any::class.java)
            methodGet.setAccessible(true)
            //获取到observer在集合中对应的ObserverWrapper对象
            val objectWrapperEntry = methodGet.invoke(observers, observer)
            var objectWrapper : Any?= null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = objectWrapperEntry.value
            }
            if (objectWrapper == null) {
                throw NullPointerException("ObserverWrapper can not be null")
            }
            //获取ObserverWrapper的Class对象  LifecycleBoundObserver extends ObserverWrapper
            val wrapperClass: Class<*> = objectWrapper.javaClass.superclass
            //获取ObserverWrapper的field mLastVersion
            val mLastVersion: Field = wrapperClass.getDeclaredField("mLastVersion")
            mLastVersion.setAccessible(true)
            //获取liveData的field mVersion
            val mVersion: Field = liveDataClass.getDeclaredField("mVersion")
            mVersion.setAccessible(true)
            val mV: Any = mVersion.get(this)
            //把当前ListData的mVersion赋值给 ObserverWrapper的field mLastVersion
            mLastVersion.set(objectWrapper, mV)
            mObservers.setAccessible(false)
            methodGet.setAccessible(false)
            mLastVersion.setAccessible(false)
            mVersion.setAccessible(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}