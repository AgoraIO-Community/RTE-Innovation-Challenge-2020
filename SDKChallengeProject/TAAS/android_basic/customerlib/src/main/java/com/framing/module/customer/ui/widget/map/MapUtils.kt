package com.framing.module.customer.ui.widget.map

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


}