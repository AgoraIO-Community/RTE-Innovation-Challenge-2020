package com.framing.module.customer.ui.widget.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.framing.baselib.TLog
import com.framing.commonlib.map.SimpleMapView
import com.framing.commonlib.utils.DisplayUtils
import com.framing.commonlib.utils.FileUtils
import com.framing.commonlib.utils.ImageUtils
import com.framing.module.customer.R
import com.framing.module.customer.ui.widget.map.draw.DrawConfig
import com.young.businessmvvm.data.repository.network.NetRequestManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException


/*
 * Des  负载客户端业务的地图view
 * 父类SimpleMapView 负载生命周期
 * 祖父类FramingMapView 负载高德mapview
 * 维护对应分层
 * Author Young
 * Date 
 */
class CustomerMapView : SimpleMapView ,AMap.InfoWindowAdapter,AMap.OnInfoWindowClickListener,AMap.OnMarkerClickListener{
    constructor(p0: Context?) : super(p0){
        addView()
    }
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1){
        addView()
    }
    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2)

    private val TEST_AREA_BR=LatLng(37.30299398093054,112.02101053883912)//底左
    private val TEST_AREA_BL=LatLng(37.30313052744131,112.01696442660936)//底右
    private val TEST_AREA_TL=LatLng(37.30569072861245,112.01740833219374)//上左
    private  val TEST_AREA_TR=LatLng(37.30567792782341,112.02136056710957)//上右
    private  val TEST_AREA_CENTER=LatLng(37.3043,112.019107)//聚焦点 //17级
    private val TEST_UAV_PRO=LatLng(37.30431463131708,112.0191075115756)//uav植保
    private val TEST_UAV_WATCH=LatLng(37.304451175429904,112.02055992773232)//uav植保
    private val TEST_TRACK_PRO=LatLng(37.30461332124187,112.01809900100325)//track植保


    private val localPath="/storage/emulated/0/TAAS_C/mapCache"
    private val googleUrl="http://mt2.google.cn/vt/lyrs=y&scale=2&hl=zh-CN&gl=cn&x=%d&s=&y=%d&z=%d"
    private var mFileDirName:String?=null
    private var mFileName:String?=null
    private var isLock=true//锁定map
    private var maker:Marker?=null//添加的当前操作merker
    private var infoWindowView:MarkerView?=null//确保 getInfoContents 和infowindow 一个view
    private val markerOption = MarkerOptions()

    private fun addView(){
        TLog.log("map_addView","1111")
        val params=LayoutParams(DisplayUtils.dp2px(35f),DisplayUtils.dp2px(35f))
        params.setMargins(0, DisplayUtils.dp2px(15f), DisplayUtils.dp2px(15f), 0)
        params.gravity=Gravity.RIGHT
        val lockView= ImageView(context)
        lockView.setImageResource(R.mipmap.audio_bottom_paue_icon)
        lockView.layoutParams=params
        addView(lockView)
        infoWindowView=MarkerView(context)
        map.setInfoWindowAdapter(this@CustomerMapView)
        map.setOnMarkerClickListener(this@CustomerMapView)
        map.setOnInfoWindowClickListener(this@CustomerMapView)
        isClickable=true
    }

    override fun urlFilePath(x: Int, y: Int, zoom: Int): String {
        mFileDirName = String.format("L%02d/", zoom + 1)
        mFileName = String.format(
            "%s",
            MapUtils.tileXYToQuadKey(x, y, zoom)
        ) //为了不在手机的图片中显示,取消jpg后缀,文件名自己定义,写入和读取一致即可,由于有自己的bingmap图源服务,所以此处我用的bingmap的文件名
        val LJ: String = localPath+File.separator + mFileDirName + mFileName
        TLog.log("customermapview",LJ+"___"+FileUtils.isFileExists(LJ))
        if(FileUtils.isFileExists(LJ)){
            //判断本地是否有图片文件,如果有返回本地url,如果没有,缓存到本地并返回googleurl
            return "file://$LJ"
        }else{
            val filePath = String.format(googleUrl, x, y, zoom)
            val mBitmap: Bitmap
            mBitmap = ImageUtils.getBitmap(NetRequestManager.get().getMap(filePath))
            try {
                var file= File(localPath +File.separator+ mFileDirName + mFileName);
                var a= ImageUtils.save(mBitmap,
                    file,Bitmap.CompressFormat.JPEG
                )
                TLog.log("customermapview",a.toString()+mBitmap+"__${file.path}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return filePath
        }
        return ""
    }

    override val diskCacheDir: String
        get() =localPath

    override fun drawOverlay() {
        GlobalScope.launch {
            Thread.sleep(3000)
            withContext(Dispatchers.IO){
                Thread.sleep(3000)
                withContext(Dispatchers.Main){
                    //创建测试区域
                    MapDrawBuild()
                        .with(map)
                        .style(MapDrawBuild.DrawMapStye.DRAW_RECTANGLE)
                        .drawConfig(DrawConfig(Color.TRANSPARENT,Color.parseColor("#8072d2f5"),10f,999f))
                        .targetLatLng(listOf(TEST_AREA_TL,TEST_AREA_TR,TEST_AREA_BR,TEST_AREA_BL),1)
                        .create()
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun addMaker(mAmap:AMap) {
        //初始化makerview
        for(i in 3 downTo 1){
            TLog.log("downto",i.toString())
            if(i==1) {
                infoWindowView?.viewType(MarkerView.MakerType.UAV_WATCH)
                makerLogic("","",TEST_UAV_WATCH,true)
            }else if(i==2){
                infoWindowView?.viewType(MarkerView.MakerType.UAV_PRO)
                makerLogic("","", TEST_UAV_PRO,false)
            }else{
                infoWindowView?.viewType(MarkerView.MakerType.Track_PRO)
                makerLogic("", "",TEST_TRACK_PRO,false)
            }

        }
    }
    /*
    * 如果返回的View不为空且View的background不为null，
    * 则直接使用它来展示marker的信息。如果backgound为null
    * ，SDK内部会给这个View设置一个默认的background。
    * 如果这个方法返回null，将使用内置的一个默认的View来展示marker的信息。
    * */
    private fun makerLogic(title:String,content:String,latLng: LatLng,isInfo:Boolean){
        markerOption.position(latLng)
            .draggable(true) //设置Marker可拖动
            .setFlat(true)
            .infoWindowEnable(true)
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.mark_location))
        maker= map.addMarker(markerOption)
        if(isInfo) {
            maker?.showInfoWindow()
        }
    }
    override fun moveCamera(mAmap: AMap,delay:Int) {
        GlobalScope.launch {
            Thread.sleep(delay.toLong())
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    TEST_AREA_CENTER, 17f
                )//放大级别
            )
        }
    }
    override fun uiSetting(settings: UiSettings) {
        settings.run {
            isCompassEnabled=true //是否显示指南针
            isScaleControlsEnabled=true//比例尺控件是否显示
            isRotateGesturesEnabled=false//旋转手势关闭
            isZoomControlsEnabled=false//放大缩小按钮
        }
    }
    /*
    * 锁定地图 保证主页交互
    * */
    fun isLock():Boolean{
        if(isLock) {
            isLock=false
        }else{
            isLock=true
            moveCamera(map,0)
        }
        return isLock
    }
    private var mLastX:Float?=null
    private var mLastY:Float?=null


//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        val x = ev?.getX()
//        val y = ev?.getY()
//        when (ev?.getAction()) {
//            MotionEvent.ACTION_DOWN -> {
//                mLastX = ev.getX()
//                mLastY = ev.getY()
//                return super.dispatchTouchEvent(ev)
//            }
//
//            MotionEvent.ACTION_UP -> {
//                val deltaX = x?.minus(mLastX!!)
//                val deltaY = y?.minus(mLastY!!)
//                TLog.log("touch_event122","${deltaX} --- ${deltaY}")
//                if (deltaX?.toInt() ==0 && deltaY?.toInt()==0) {
//                    return true
//                }
//            }
//            else -> {
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    override fun getInfoContents(p0: Marker?): View {
        return infoWindowView!!
    }


    override fun getInfoWindow(p0: Marker?): View {
        TLog.log("getInfoWindow","$infoWindowView"+(p0?.position))
        when(p0?.position){
            TEST_TRACK_PRO->{
                TLog.log("getInfoWindow","TEST_TRACK_PRO"+(p0?.position))
                infoWindowView?.viewType(MarkerView.MakerType.Track_PRO)
            }
            TEST_UAV_PRO->{
                TLog.log("getInfoWindow","TEST_UAV_PRO"+(p0?.position))
                infoWindowView?.viewType(MarkerView.MakerType.UAV_PRO)
            }
            TEST_UAV_WATCH->{
                TLog.log("getInfoWindow","TEST_UAV_WATCH"+(p0?.position))
                infoWindowView?.viewType(MarkerView.MakerType.UAV_WATCH)
            }
        }
        return  infoWindowView!!
    }
    /*
    * -----start-----
    * info click  marker click
    *
    * */
    override fun onInfoWindowClick(p0: Marker?) {
        TLog.log("onInfoWindowClick","$p0")
        when(p0?.position){
            TEST_TRACK_PRO->{
            }
            TEST_UAV_PRO->{
            }
            TEST_UAV_WATCH->{
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TLog.log("onMarkerClick","$p0")
        when(p0?.position){
            TEST_TRACK_PRO->{
                makerLogic("","",TEST_TRACK_PRO,true)
            }
            TEST_UAV_PRO->{
                makerLogic("","",TEST_UAV_PRO,true)
            }
            TEST_UAV_WATCH->{
                makerLogic("","",TEST_UAV_WATCH,true)
            }
        }
        return true
    }
}