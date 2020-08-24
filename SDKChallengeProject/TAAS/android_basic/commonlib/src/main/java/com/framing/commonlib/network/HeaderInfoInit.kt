package com.framing.commonlib.network

import com.framing.commonlib.BuildConfig
import java.util.HashMap

/*
 * Des  构建header 信息
 * Author Young
 * Date 
 */class HeaderInfoInit :IRequestHeaderInfo {
    var header = HashMap<String, String>()

    override fun getRequestHeaderMap(): HashMap<String, String> {
        return header
    }
    override fun isDebug(): Boolean {
       return BuildConfig.DEBUG
    }
}