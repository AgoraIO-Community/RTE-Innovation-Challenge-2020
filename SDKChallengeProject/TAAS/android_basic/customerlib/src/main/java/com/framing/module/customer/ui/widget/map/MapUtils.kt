package com.framing.module.customer.ui.widget.map

import android.content.Context
import com.amap.api.services.weather.LocalWeatherForecastResult
import com.amap.api.services.weather.LocalWeatherLiveResult
import com.amap.api.services.weather.WeatherSearch
import com.amap.api.services.weather.WeatherSearchQuery
import com.framing.baselib.TLog
import com.framing.module.customer.data.bean.WeatherLiveData

/*
 * Des  
 * Author Young
 * Date 
 */object MapUtils {

    private var stringBuilder:StringBuilder?=null

    /**
     * 瓦片数据坐标转换
     */
    fun  tileXYToQuadKey(tileX:Int,tileY:Int,levelOfDetail:Int):String{
        stringBuilder =StringBuilder()
        for (index in levelOfDetail downTo 0 ){
            var digit = '0'
            val mask = 1 shl index - 1
            if (tileX and mask != 0) {
                digit++
            }
            if (tileY and mask != 0) {
                digit++
                digit++
            }
            stringBuilder?.append(digit)
        }
        return stringBuilder?.toString()!!
    }

    /*
    * 天气查询
    * placeName 查询地区
    * context  依赖上下文
    * method 回调
    * */
    fun queryWeather(placeName:String,context:Context,method: (WeatherLiveData) -> Unit){
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        val  mquery = WeatherSearchQuery(placeName, WeatherSearchQuery.WEATHER_TYPE_LIVE)
        val mweathersearch = WeatherSearch(context)
        mweathersearch.setOnWeatherSearchListener(object :WeatherSearch.OnWeatherSearchListener{
            override fun onWeatherLiveSearched(p0: LocalWeatherLiveResult?, p1: Int) {
                if(p1==1000){
                    p0?.run {
                        val weather=this.liveResult.weather+"        ${this.liveResult.temperature}度"
                        val humidity="湿度    ${this.liveResult.humidity}%"
                        val wind="${this.liveResult.windDirection}风    ${this.liveResult.windPower}级"
                        val time=this.liveResult.reportTime
                        val liveData=WeatherLiveData(weather,humidity,wind,time)
                        method(liveData)
                    }
                }else{
                    TLog.log("AMAP_WEATHER_ERROR$p1")
                }
            }
            override fun onWeatherForecastSearched(p0: LocalWeatherForecastResult?, p1: Int) {
                p0?.run {
                    TLog.log("map_weather","" +
                            "${this.forecastResult.weatherForecast[0].week} " +
                            "${this.forecastResult.weatherForecast.size}" + "")
                }
            }
        })
        mweathersearch.setQuery(mquery)
        mweathersearch.searchWeatherAsyn() //异步搜索

    }
}