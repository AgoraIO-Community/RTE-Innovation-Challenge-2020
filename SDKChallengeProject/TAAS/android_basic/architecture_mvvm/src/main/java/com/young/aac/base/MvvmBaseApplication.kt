package com.young.aac.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.framing.commonlib.base.CommonApplication

open class MvvmBaseApplication : CommonApplication(), ViewModelStoreOwner {
    private var mAppViewModelStore: ViewModelStore? = null
    private var mFactory: ViewModelProvider.Factory? = null
    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        sApplication = this
    }

    companion object {
        var sApplication: MvvmBaseApplication? = null
        var sIsDebug = false
        fun setIsDebug(isDebug: Boolean) {
            sIsDebug = isDebug
        }

        /**
         * 获取进程名
         *
         * @param context
         * @return
         */
        fun getCurrentProcessName(context: Context): String? {
            val am =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningApps =
                am.runningAppProcesses ?: return null
            for (proInfo in runningApps) {
                if (proInfo.pid == Process.myPid()) {
                    if (proInfo.processName != null) {
                        return proInfo.processName
                    }
                }
            }
            return null
        }
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore!!
    }

    open fun getAppViewModelProvider(activity: Activity): ViewModelProvider? {
        return ViewModelProvider(
            activity.applicationContext as MvvmBaseApplication,
            (activity.applicationContext as MvvmBaseApplication).getAppFactory(activity)
        )
    }

    open fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        val application = checkApplication(activity)
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory!!
    }

    open fun checkActivity(fragment: Fragment): Activity {
        return fragment.activity
            ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
    }

    open fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw java.lang.IllegalStateException(
                "You can't request ViewModel before onCreate call."
            )
    }
}