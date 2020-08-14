package com.framing.support.map

import android.content.Context
import android.util.AttributeSet
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.TileOverlay
import com.amap.api.maps.model.TileOverlayOptions
import com.amap.api.maps.model.UrlTileProvider
import java.net.URL

/*
 * Des  依赖支持的封装层 暴露出去
 * Author Young
 * Date 
 */
abstract class FramingMapView : MapView {
    constructor(p0: Context?) : super(p0){
    }
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1){
    }

    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2){
    }
    constructor(p0: Context?, p1: AMapOptions?) : super(p0, p1){
    }

    private var url =
        "http://mt2.google.cn/vt/lyrs=y&scale=2&hl=zh-CN&gl=cn&x=%d&s=&y=%d&z=%d"
    private lateinit var OMCMapOverlay: TileOverlay//覆盖的瓦片
    private var longitude=112.019825//经度
    private var latitude=37.303396//纬度

    abstract fun urlFilePath( x:Int,  y:Int,  zoom:Int):String//实时操作获取对应google瓦片
    abstract val diskCacheDir:String//缓存路径


    /*
    * TileOverlayOptions overlayOptions 覆盖物的设置构造
    * 把加载好瓦片的overlayOptions  添加给Amap
    * */
    fun initLoad(){
        initAmap()
        var overlayOptions=TileOverlayOptions().tileProvider(
            object :UrlTileProvider(256, 256){
                override fun getTileUrl(p0: Int, p1: Int, p2: Int): URL? {
                    return URL(urlFilePath(p0,p1,p2))
                }
            })
        overlayOptions.diskCacheEnabled(false) //由于自带的缓存在关闭程序后会自动释放,所以无意义,关闭本地缓存
            .diskCacheDir(diskCacheDir)
            .diskCacheSize(1024000)
            .memoryCacheEnabled(true)
            .memCacheSize(102400)
            .zIndex(-9999f)
        OMCMapOverlay = map.addTileOverlay(overlayOptions)
    }
    private fun initAmap(){
        //移动中心点
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latitude,
                    longitude
                ), 17f
            )//放大级别
        )
        map.uiSettings.run {
            isCompassEnabled=true //是否显示指南针
            isScaleControlsEnabled=true//比例尺控件是否显示
            isRotateGesturesEnabled=false//旋转手势关闭
        }

    }
    //清除覆盖物
    fun cleanOverlay(){
        OMCMapOverlay?.remove()
    }
    //重新加载覆盖物
    fun reloadOverLay(){
        initLoad()
    }
}