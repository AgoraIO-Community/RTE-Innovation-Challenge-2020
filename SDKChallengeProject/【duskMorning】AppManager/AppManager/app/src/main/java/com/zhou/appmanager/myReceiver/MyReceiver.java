package com.zhou.appmanager.myReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhou.appmanager.MainActivity;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            //Activity activity= (Activity) context;
            //activity.finish();
            context.startActivity(new Intent(context,MainActivity.class));

        }
    }
}
