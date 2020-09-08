package com.framing.module.customer.ui.widget.map

import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.framing.module.customer.ui.widget.map.draw.DrawConfig
import com.framing.module.customer.ui.widget.map.draw.RectangleDraw
import java.lang.Exception

/*
 * Des 绘制物的基础构建类
 * Author Young
 * Date 
 */class MapDrawBuild {
    constructor()
    private var mAMap:AMap?=null
    private var style:DrawMapStye?=null //绘制物的种类
    private var latLngs:List<LatLng>?=null//绘制的目标 点 - 点成线
    private var offSet:Int?=null//绘制连线的偏移
    private var config:DrawConfig?=null//绘制配置 例如颜色等


    /*
    * Amap
    * */
    fun with(aMap:AMap):MapDrawBuild{
        mAMap=aMap
       return this
    }
    /*
    * DrawMapStye
    * DRAW_RECTANGLE 矩形
    * */
    fun style(drawMapStye: DrawMapStye):MapDrawBuild{
        style=drawMapStye
        return this
    }
    /*
    *
    * */
    fun drawConfig(config: DrawConfig):MapDrawBuild{
        this.config=config
        return this
    }
    /*
    * LatLng 参照点经纬度数据  不规则则是数据集
    *
   * */
    fun targetLatLng(datas: List<LatLng>,offSet:Int):MapDrawBuild{
        latLngs=datas
        this.offSet=offSet
        return this
    }
    fun create(){
        drawLogic()
    }

    private fun drawLogic() {
        if(mAMap==null||style==null||latLngs==null){
            throw Exception("BaseMapDrawBuild create fail $mAMap $style $latLngs")
        }
        when(style){
            DrawMapStye.DRAW_RECTANGLE-> run {
                RectangleDraw()?.draw(mAMap!!, latLngs!!, 1, config)
            }
        }
    }


    enum class DrawMapStye{
        DRAW_RECTANGLE,
        DRAW_ROUND
    }
}