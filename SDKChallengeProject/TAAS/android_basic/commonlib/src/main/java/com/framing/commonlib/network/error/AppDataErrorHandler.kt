package com.framing.commonlib.network.error

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.functions.Function
import org.json.JSONObject
import java.util.*

/**
 * 服务器内部错误业务逻辑 app code 错误 处理
 * HandleFuc处理以下网络错误：
 */
class AppDataErrorHandler<T> :
    Function<Object , Object> {
    override fun apply(t: Object): Object{
        var `object`: JSONObject? = null
        //app 服务器内部错误业务逻辑因该写在Appdataerrorhandler
        val gson = Gson()
        val a = gson.toJson(t)
        `object` = JSONObject(a)
        val code = `object`.optInt("code")
        val message = `object`.optString("message")
        return t
    }
}