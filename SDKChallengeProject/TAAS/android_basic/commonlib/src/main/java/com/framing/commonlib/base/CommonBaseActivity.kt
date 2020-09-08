package com.framing.commonlib.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.framing.commonlib.helper.ProjectLiveData
import com.framing.commonlib.inject.policy.PermissionPolicy
import com.framing.commonlib.utils.DealWithPermissions.*

/*
* Common级别activity 封装  例如基础业务 点击统计
* 这里有一个使用kotlin 构造 响应式权限请求
* */
open class CommonBaseActivity :AppCompatActivity {
    constructor() : super()
    var permissionTag=ProjectLiveData<Int>()//根据permissionresult 触发permisssionRun

    /*
    * 使用kotlin 构造 响应式权限请求
    * 权限运行 拥有权限复归逻辑 否则请求权限
    * 权限请求成功后响应试触发 mt 后面逻辑
    * */
    inline fun  <T : CommonBaseActivity> T.permissionRun(policy: PermissionPolicy, crossinline mt: () -> Unit) {
        var a = arrayOf<String>()//具体权限
        var code=STORAGE_REQUEST_CODE
        when(policy){
            PermissionPolicy.P_STO->{
                a= STORAGE
            }
            PermissionPolicy.P_MIC->{
                a= MICROPHONE
                code= MICROPHONE_REQUEST_CODE
            }
            PermissionPolicy.P_CAM->{
                a= CAMERAS
                code= CAMERAS_REQUEST_CODE
            }
            PermissionPolicy.P_LOC->{
                a= LOCATION
                code= LOCATION_REQUEST_CODE
            }
        }
        if (getInstance(this).checkPermision(a)) mt()
        else {
            getInstance(this).requestSpecificPermissions(a)
            permissionTag.observe(this, Observer{
                if(code==it){
                    mt()
                }
            })
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.size > 0) && (grantResults.get(0) == android.content.pm.PackageManager.PERMISSION_GRANTED))
        {
            permissionTag.value=requestCode
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}