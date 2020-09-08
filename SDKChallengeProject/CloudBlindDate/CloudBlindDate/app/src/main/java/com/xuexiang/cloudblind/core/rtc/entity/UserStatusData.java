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

package com.xuexiang.cloudblind.core.rtc.entity;

import android.view.SurfaceView;

public class UserStatusData {
    public static final int DEFAULT_STATUS = 0;
    public static final int VIDEO_MUTED = 1;
    public static final int AUDIO_MUTED = VIDEO_MUTED << 1;

    public static final int DEFAULT_VOLUME = 0;

    public UserStatusData(int uid, SurfaceView view, Integer status, int volume) {
        this(uid, view, status, volume, null);
    }

    public UserStatusData(int uid, SurfaceView view, Integer status, int volume, VideoInfoData i) {
        this.mUid = uid;
        this.mView = view;
        this.mStatus = status;
        this.mVolume = volume;
        this.mVideoInfo = i;
    }

    public int mUid;

    public SurfaceView mView;

    public Integer mStatus; // if status is null, do nothing

    public int mVolume;

    private VideoInfoData mVideoInfo;

    public void setVideoInfo(VideoInfoData video) {
        mVideoInfo = video;
    }

    public VideoInfoData getVideoInfoData() {
        return mVideoInfo;
    }

    @Override
    public String toString() {
        return "UserStatusData{" +
                "mUid=" + (mUid & 0XFFFFFFFFL) +
                ", mView=" + mView +
                ", mStatus=" + mStatus +
                ", mVolume=" + mVolume +
                '}';
    }
}
