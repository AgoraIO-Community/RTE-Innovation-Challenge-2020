package com.xiaoyang.poweroperation.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.xiaoyang.poweroperation.app.AppApplication;
import com.xiaoyang.poweroperation.data.entity.LocationBean;

/**
 * ProjectName: android_source
 * CreateDate: 2019-07-22
 * ClassName: LocationUtils
 * Author: xiaoyangyan
 * note 定位工具类
 */
public class LocationUtils {
    @SuppressLint("StaticFieldLeak")
    private static AMapLocationClient mLocationClient;
    private static AMapLocationClientOption mLocationOption = null;
    public static AMapLocation sLocation = null;

    private static class LocationHolder {
        private static final LocationUtils INSTANCE = new LocationUtils();
    }

    public static LocationUtils getInstance() {
        return LocationHolder.INSTANCE;
    }

    public static void init(Context context) {
        mLocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    public interface MyLocationListener {
        void result(AMapLocation location);
    }

    // 获取之前定位位置,如果之前未曾定位,则重新定位
    public static void getLocation(MyLocationListener listener) {
        if (sLocation == null) {
            startLocalService(listener);
        } else {
            listener.result(sLocation);
        }
    }

    public static void startLocalService(final MyLocationListener listener) {
        if (mLocationClient == null) {
            return;
        }

        LocationBean locationBean = new LocationBean();
        mLocationClient.setLocationListener(location -> {
            if (null != location) {
                mLocationClient.stopLocation();
                sLocation = location;
                listener.result(location);
                if (location.getErrorCode() == 0) {
                    locationBean.setLongitude(String.valueOf(location.getLongitude()));
                    locationBean.setLatitude(String.valueOf(location.getLatitude()));
                    locationBean.setAddress(location.getAddress());
//                    RxBus.getDefault().post("GetLocation", locationBean);
                    Log.v("yxy", "定位成功：" + location.getAddress());
                } else {
                    Log.v("yxy", "定位失败\n错误码：" + location.getErrorCode()
                            + "\n错误信息:" + location.getErrorInfo()
                            + "\n错误描述:" + location.getLocationDetail());
                }
            } else {
                Toast.makeText(AppApplication.getInstance().getApplicationContext(), "定位失败，loc is null", Toast.LENGTH_SHORT).show();
            }
        });
        mLocationClient.startLocation();

    }

    public void stopLocalService() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient.stopLocation();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }
}
