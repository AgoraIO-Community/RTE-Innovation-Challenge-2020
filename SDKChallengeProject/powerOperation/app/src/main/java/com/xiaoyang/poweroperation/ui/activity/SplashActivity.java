package com.xiaoyang.poweroperation.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.account.LoginActivity;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends AppCompatActivity {
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        showPermissionSuccess();
    }


    /**
     * 权限操作
     */
    @SuppressLint("CheckResult")
    public void showPermissionSuccess() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.setLogging(true);
        permissions.requestEachCombined(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        Log.v("yxy", "permission is finished");
                        if (BmobUser.isLogin()) {
                            ArmsUtils.startActivity(MainActivity.class);
                        }else {
                            gotoLoginView();
                        }

                    }
                });
    }


    /**
     * 跳转到主页
     */
    private void gotoLoginView() {
        startTime = System.currentTimeMillis();
        long currentTimeMillis = System.currentTimeMillis();
        long delayed = 2000 - (currentTimeMillis - startTime);
        if (delayed < 0) {
            delayed = 0;
        }
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, delayed);
    }
}