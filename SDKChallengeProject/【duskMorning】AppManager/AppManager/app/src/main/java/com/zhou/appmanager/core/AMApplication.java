package com.zhou.appmanager.core;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class AMApplication extends Application {
    private static AMApplication sInstance;
    private ChatManager mChatManager;


    public static AMApplication the() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mChatManager = new ChatManager(this);
        mChatManager.init();
    }

    public ChatManager getChatManager() {
        return mChatManager;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

