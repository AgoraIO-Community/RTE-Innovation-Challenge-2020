/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.cloudblind.core.rtc;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.xuexiang.cloudblind.R;
import com.xuexiang.cloudblind.core.rtc.entity.CurrentUserSettings;
import com.xuexiang.cloudblind.core.rtc.entity.EngineConfig;
import com.xuexiang.xutil.common.logger.Logger;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

/**
 * @author xuexiang
 * @since 2020/9/7 12:04 AM
 */
public class RtcManager {

    private static volatile RtcManager sInstance = null;

    private CurrentUserSettings mVideoSettings = new CurrentUserSettings();

    private RtcEngine mRtcEngine;
    private EngineConfig mConfig;
    private MyEngineEventHandler mEventHandler;

    private RtcManager() {

    }

    /**
     * 获取单例
     */
    public static RtcManager getInstance() {
        if (sInstance == null) {
            synchronized (RtcManager.class) {
                if (sInstance == null) {
                    sInstance = new RtcManager();
                }
            }
        }
        return sInstance;
    }

    public void initRtcEngine(Application application) {
        String appId = application.getString(R.string.agora_app_id);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
        }
        mEventHandler = new MyEngineEventHandler();
        try {
            // Creates an RtcEngine instance
            mRtcEngine = RtcEngine.create(application, appId, mEventHandler);
        } catch (Exception e) {
            Logger.e(e);
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        mRtcEngine.enableVideo();
        mRtcEngine.enableAudioVolumeIndication(200, 3, false);

        mConfig = new EngineConfig();
    }


    public EngineConfig getConfig() {
        return mConfig;
    }

    public RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    public void addEventHandler(AGEventHandler handler) {
        mEventHandler.addEventHandler(handler);
    }

    public void remoteEventHandler(AGEventHandler handler) {
        mEventHandler.removeEventHandler(handler);
    }

    public CurrentUserSettings userSettings() {
        return mVideoSettings;
    }

}
