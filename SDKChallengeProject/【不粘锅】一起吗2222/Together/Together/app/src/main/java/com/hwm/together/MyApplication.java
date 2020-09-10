package com.hwm.together;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.hwm.together.bean.FriendInfoBean;
import com.hwm.together.bean.UserInfo;
import com.hwm.together.greendao.DaoMaster;
import com.hwm.together.greendao.DaoSession;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private List<Activity> activityList = new ArrayList<Activity>();

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static UserInfo mUserInfo;
    public static List<FriendInfoBean> mFriendsList = new ArrayList<>();

    @Override
    public void onCreate() {

//        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        super.onCreate();
        myApplication = this;
//        mUserInfo = new UserInfo();
//        mUserInfo.setAccount("19951684702");
        com.igexin.sdk.PushManager.getInstance().initialize(this);
        setDatabase();
    }

    public static MyApplication getInstance(){
        if(null == myApplication){
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    public static void newUserInfo(){
        mUserInfo = new UserInfo();
    }

    public static void setmFriendsList(List<FriendInfoBean> friendsList){
        mFriendsList = friendsList;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public void exit(){
        for (Activity activity: activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    //初始化数据库
    private void setDatabase(){
        mHelper = new DaoMaster.DevOpenHelper(this,"manyou-db",null);
        db = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }
    public SQLiteDatabase getDb(){
        return db;
    }
}
