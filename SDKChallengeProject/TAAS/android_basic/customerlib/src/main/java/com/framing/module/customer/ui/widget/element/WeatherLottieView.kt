package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView

/*
 * Des  依托于萝莉妹妹view 玩转气象 感谢jochang 开放得 lottie ani
 * Author Young
 * Date 
 */class WeatherLottieView : LottieAnimationView{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun weatherStyle(style:WeatherStyle){
        when(style){//可区分黑白天
            WeatherStyle.WT_SUNNY->{
                setAnimation("weather_sunny.json")
            }
            WeatherStyle.WT_CLOUDY->{
                setAnimation("weather_partly_cloudy.json")
            }
            WeatherStyle.WT_WINDY->{
                setAnimation("weather_windy.json")
            }
            WeatherStyle.WT_THOUNDER->{
                setAnimation("weather_thunder.json")
            }
            WeatherStyle.WT_STORM_THOUNDER->{
                setAnimation("weather_storm.json")
            }
            WeatherStyle.WT_SNOW_SUNNY->{
                setAnimation("weather_snow_sunny.json")
            }
            WeatherStyle.WT_MIST->{
                setAnimation("weather_mist.json")
            }
        }
    }

    enum class WeatherStyle{
        WT_SUNNY,//晴天
        WT_CLOUDY,//多云/阴
        WT_WINDY,//多风
        WT_THOUNDER,//雷阵雨
        WT_STORM_THOUNDER,//暴风雨
        WT_SNOW_SUNNY,//晴转雪
        WT_MIST,//雾霾
    }
}