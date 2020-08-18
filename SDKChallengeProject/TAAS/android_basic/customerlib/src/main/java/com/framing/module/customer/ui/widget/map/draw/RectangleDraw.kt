package com.framing.module.customer.ui.widget.map.draw

import android.graphics.Color
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.PolygonOptions




/*
 * Des  
 * Author Young
 * Date 
 */class RectangleDraw : BaseDrawOverlay {

    override fun draw(aMap: AMap, latLng: List<LatLng>, offSet: Int) {
//        aMap.addPolygon(PolygonOptions()
//            .addAll(latLng)
//            .fillColor(Color.GREEN)
//            .strokeColor(Color.RED)
//            .strokeWidth(1f))
        // 声明 多边形参数对象
        val polygonOptions = PolygonOptions()
        // 添加 多边形的每个顶点（顺序添加）
        // 添加 多边形的每个顶点（顺序添加）
        polygonOptions.add(latLng[0], latLng[1], latLng[2], latLng[3])
        polygonOptions.strokeWidth(15f) // 多边形的边框
            .strokeColor(Color.GREEN) // 边框颜色
            .fillColor(Color.GRAY) // 多边形的填充色
        aMap.addPolygon(polygonOptions)
    }

    private fun createFourPoint( latLng: List<LatLng>, offSet: Int):List<LatLng>{
        val datas = arrayListOf<LatLng>()
        val target=latLng.get(0)
        datas.add(LatLng(target.latitude-offSet,target.longitude))
        return datas
    }
}