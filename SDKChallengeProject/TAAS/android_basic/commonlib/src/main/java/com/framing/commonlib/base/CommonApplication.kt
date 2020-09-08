package com.framing.commonlib.base

import android.app.Application
/*
* Common级别application 封装
* */
open class CommonApplication : Application{
    constructor() : super()

    override fun onCreate() {
        super.onCreate()
    }
}