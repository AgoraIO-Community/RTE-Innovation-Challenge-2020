package com.framing.commonlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;


/**
 * Created by ${Young} on 2017/10/30.
 * fragment 使用回掉在actvity  如果想在framgnet 回掉 在fragment使用requestpermision
 */

public class DealWithPermissions {
    //兼容8.0 组权限
    public static final String[] PHONE = new String[] {//电话相关
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
    };
    public static final  String[] SMS = new String[] {//短信相关
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS
    };
    public static String[] STORAGE = new String[] {//存储
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String[] SENSORS = new String[] {//传感器
            Manifest.permission.BODY_SENSORS
    };
    public static final String[] MICROPHONE = new String[] {//麦克分
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static String[] LOCATION = new String[] {//定位
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String[] CONTACTS = new String[] {//联系人
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS
    };
    public static final String[] CAMERAS = new String[] {//相机
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String[] CALENDARS = new String[] {//联系人
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    // dangero
    // us permission  空行为一组全限 组权限只申请一条就可以

    public static final int STORAGE_REQUEST_CODE=100;//存储请求值
    public static final int MICROPHONE_REQUEST_CODE=300;//麦克分请求值
    public static final int CAMERAS_REQUEST_CODE=200;//录音请求值
    public static final int LOCATION_REQUEST_CODE=400;//录音请求值

    // dangerous permission  空行 为一组全限 组权限只申请一条就可以
    private static  DealWithPermissions instance;
    private static Activity mActivity;
    private int targetSdkVersion;
    private PackageInfo info;
    public static DealWithPermissions getInstance(Activity aInstance){
        mActivity =aInstance;
        if (instance == null) {
            instance = new DealWithPermissions();
        }
        return instance;
    }
    public DealWithPermissions() {
        try {
            info = mActivity.getPackageManager().getPackageInfo(
                    mActivity.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void requestSpecificPermissions(String [] PERMISSIONS){//请求部分需求得权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M||mActivity==null) {
            return ;
        }
        int requestCode=100;
        if(PERMISSIONS==STORAGE){
            requestCode=STORAGE_REQUEST_CODE;
        }else if(PERMISSIONS==CAMERAS){
            requestCode=CAMERAS_REQUEST_CODE;
        }else if(PERMISSIONS==MICROPHONE){
            requestCode=MICROPHONE_REQUEST_CODE;
        }
        startDealWith(PERMISSIONS,requestCode);
    }

    public void requestSpecificPermissions(String [] PERMISSIONS,int resultCode){//单activity 多次单权限判断 resultcode
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return ;
        }
        int requestCode=resultCode;
        startDealWith(PERMISSIONS,requestCode);
    }
    /**
     * @param PERMISSIONS
     *  @param requestCode 请求值用作回掉处理
     */
    private void startDealWith(String [] PERMISSIONS,int requestCode) {
        for(String rPermission:PERMISSIONS){
            Boolean isGranted=true;//允许
            if (targetSdkVersion >= Build.VERSION_CODES.M) {//解决targetsdk<23 权限判断一直是true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    isGranted = mActivity.checkSelfPermission(rPermission)
                            == PackageManager.PERMISSION_GRANTED;
                }
            } else {
                isGranted = PermissionChecker.checkSelfPermission(mActivity, rPermission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
            if(!isGranted){
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        mActivity,
                        PERMISSIONS,
                        requestCode
                );
                mActivity=null;
                return;
            }
        }
        mActivity=null;
        return;
    }
    private void startDealWith(String [] PERMISSIONS) {
        int requestCode=100;
        for(String rPermission:PERMISSIONS){
            Boolean isGranted=true;//允许
            if (targetSdkVersion >= Build.VERSION_CODES.M) {//解决targetsdk<23 权限判断一直是true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    isGranted = mActivity.checkSelfPermission(rPermission)
                            == PackageManager.PERMISSION_GRANTED;
                }
            } else {
                isGranted = PermissionChecker.checkSelfPermission(mActivity, rPermission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
            if(!isGranted){
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        mActivity,
                        PERMISSIONS,
                        requestCode
                );
                return;
            }
        }
        return;
    }

    public Boolean checkPermision(String manifestName){
        Boolean isGranted=false;
        if (targetSdkVersion >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                isGranted = mActivity.checkSelfPermission(manifestName)
                        == PackageManager.PERMISSION_GRANTED;
            }
        } else {//解决targetsdk<23 权限判断一直是true
            isGranted = PermissionChecker.checkSelfPermission(mActivity, manifestName)
                    == PermissionChecker.PERMISSION_GRANTED;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        mActivity=null;
        return isGranted;
    }
    public Boolean checkPermision(String[] manifestNames){
        Boolean isGranted=false;
        if (targetSdkVersion >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for(String manifestName:manifestNames ) {
                    isGranted = mActivity.checkSelfPermission(manifestName)== PackageManager.PERMISSION_GRANTED;
                    if(!isGranted){
                        break;
                    }
                }
            }
        } else {//解决targetsdk<23 权限判断一直是true
            for(String manifestName:manifestNames ) {
                isGranted = PermissionChecker.checkSelfPermission(mActivity, manifestName)
                        == PermissionChecker.PERMISSION_GRANTED;
                if(!isGranted){
                    break;
                }
            }
        }
        mActivity=null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return isGranted;
    }

}
