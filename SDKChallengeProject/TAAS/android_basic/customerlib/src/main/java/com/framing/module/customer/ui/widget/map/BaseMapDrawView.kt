package com.framing.module.customer.ui.widget.map

import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.framing.module.customer.ui.widget.map.draw.RectangleDraw
import java.lang.Exception

/*
 * Des 绘制物的基础构建类
 * Author Young
 * Date 
 */class BaseMapDrawBuild {
    constructor()
    private var mAMap:AMap?=null
    private var style:DrawMapStye?=null
    private var latLngs:List<LatLng>?=null
    private var offSet:Int?=null



    /*
    * Amap
    * */
    fun with(aMap:AMap):BaseMapDrawBuild{
        mAMap=aMap
       return this
    }
    /*
    * DrawMapStye
    * DRAW_RECTANGLE 矩形
    * */
    fun style(drawMapStye: DrawMapStye):BaseMapDrawBuild{
        style=drawMapStye
        return this
    }
    /*
    * LatLng 参照点经纬度数据  不规则则是数据集
    *
   * */
    fun targetLatLng(datas: List<LatLng>,offSet:Int):BaseMapDrawBuild{
        latLngs=datas
        this.offSet=offSet
        return this
    }
    fun create(){
        drawLogic()
    }

    private fun drawLogic() {
        if(mAMap==null&&style==null&&latLngs==null){
            throw Exception("BaseMapDrawBuild create fail $mAMap $style $latLngs")
        }
        when(style){
            DrawMapStye.DRAW_RECTANGLE-> run {
                RectangleDraw()?.draw(mAMap!!, latLngs!!, 1)
            }
        }
    }


    enum class DrawMapStye{
        DRAW_RECTANGLE,
        DRAW_ROUND
    }
}