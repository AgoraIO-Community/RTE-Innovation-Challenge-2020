package com.framing.module.customer.ui.widget.map.draw

import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng

/*
 * Des  
 * Author Young
 * Date 
 */interface BaseDrawOverlay {
        fun draw(aMap: AMap,latLng: List<LatLng>,offSet:Int,config:DrawConfig?)
}