package com.xiaoyang.poweroperation.app.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: lingjuapp
 * CreateDate: 2019-11-07
 * ClassName: SnackBarHelper
 * Author: xiaoyangyan
 * note
 */
public class SnackBarHelper {

    private Application application;
    private static List<Activity> activities = new ArrayList<>();

    public static void init(Application application){
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activities.remove(activity);
            }
        });
    }

    public static void show(String text){
        Snackbar.make(activities.get(activities.size()-1).getWindow().getDecorView(),text, Snackbar.LENGTH_SHORT).show();
    }

    public static void finishShow(String text){
        if(activities.size()>=2){
            Snackbar.make(activities.get(activities.size()-2).getWindow().getDecorView(),text,Snackbar.LENGTH_SHORT).show();
        }
    }

}
