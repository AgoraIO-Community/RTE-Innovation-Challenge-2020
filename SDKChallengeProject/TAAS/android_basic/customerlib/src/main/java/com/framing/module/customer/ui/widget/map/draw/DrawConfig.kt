package com.framing.module.customer.ui.widget.map.draw

import android.graphics.Color

/*
 * Des  绘制地图的配置参数 主要是颜色
 * Author Young
 * Date 
 */class DrawConfig {

    protected var fillColor= Color.TRANSPARENT//填充色
    protected var strokeColor=Color.BLUE//边色
    protected var strokeWidth=10F//边宽
    protected var zIndex=999f//绘制图层深度 999f 为最外层

    constructor(fillColor: Int?, strokeColor: Int?, strokeWidth: Float?, zIndex: Float?) {
        this.fillColor =if(fillColor!=null) fillColor else this.fillColor
        this.strokeColor = if(strokeColor!=null) strokeColor else this.strokeColor
        this.strokeWidth = if(strokeWidth!=null) strokeWidth else this.strokeWidth
        this.zIndex = if(zIndex!=null) zIndex else this.zIndex
    }
    fun fillColor():Int?=fillColor
    fun strokeColor():Int?=strokeColor
    fun strokeWidth():Float?=strokeWidth
    fun zIndex():Float?=zIndex

}