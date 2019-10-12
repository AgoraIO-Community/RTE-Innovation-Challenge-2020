package com.example.myapplication;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {

        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        super.onCreate();
        myApplication = this;
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
