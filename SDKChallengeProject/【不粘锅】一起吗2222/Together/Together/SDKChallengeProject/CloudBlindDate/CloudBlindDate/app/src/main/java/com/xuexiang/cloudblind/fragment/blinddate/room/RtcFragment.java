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

package com.xuexiang.cloudblind.fragment.blinddate.room;

import android.content.res.TypedArray;
import android.text.TextUtils;
import android.view.SurfaceView;

import com.xuexiang.cloudblind.R;
import com.xuexiang.cloudblind.core.BaseFragment;
import com.xuexiang.cloudblind.core.rtc.AGEventHandler;
import com.xuexiang.cloudblind.core.rtc.Constant;
import com.xuexiang.cloudblind.core.rtc.entity.EngineConfig;
import com.xuexiang.cloudblind.core.rtc.RtcManager;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xutil.common.logger.Logger;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.internal.EncryptionConfig;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

/**
 * RTC fragment
 *
 * @author xuexiang
 * @since 2020/9/7 9:01 PM
 */
public abstract class RtcFragment extends BaseFragment {

    protected final int getStatusBarHeight() {
        // status bar height
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    protected final int getActionBarHeight() {
        return ThemeUtils.resolveDimension(getContext(), R.attr.xui_actionbar_height);
    }


    /**
     * Starts/Stops the local video preview
     *
     * @param start Whether to start/stop the local preview
     * @param view  The SurfaceView in which to render the preview
     * @param uid   User ID.
     */
    protected void preview(boolean start, SurfaceView view, int uid) {
        if (start) {
            rtcEngine().setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            rtcEngine().startPreview();
        } else {
            rtcEngine().stopPreview();
        }
    }

    /**
     * Allows a user to join a channel.
     *
     * @param channel The unique channel name for the AgoraRTC session in the string format.
     * @param uid     User ID.
     */
    public final void joinChannel(final String channel, int uid) {
        String accessToken = getString(R.string.agora_access_token);
        if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "<#YOUR ACCESS TOKEN#>")) {
            accessToken = null;
        }

        rtcEngine().joinChannel(accessToken, channel, "OpenVCall", uid);
        config().mChannel = channel;
        enablePreProcessor();
        Logger.d("joinChannel " + channel + " " + uid);
    }

    /**
     * Allows a user to leave a channel.
     *
     * @param channel Channel Name
     */
    public final void leaveChannel(String channel) {
        Logger.d("leaveChannel " + channel);
        config().mChannel = null;
        disablePreProcessor();
        rtcEngine().leaveChannel();
        config().reset();
    }

    /**
     * Enables image enhancement and sets the options.
     */
    protected void enablePreProcessor() {
        if (Constant.BEAUTY_EFFECT_ENABLED) {
            rtcEngine().setBeautyEffectOptions(true, Constant.BEAUTY_OPTIONS);
        }
    }

    public final void setBeautyEffectParameters(float lightness, float smoothness, float redness) {
        Constant.BEAUTY_OPTIONS.lighteningLevel = lightness;
        Constant.BEAUTY_OPTIONS.smoothnessLevel = smoothness;
        Constant.BEAUTY_OPTIONS.rednessLevel = redness;
    }


    /**
     * Disables image enhancement.
     */
    protected void disablePreProcessor() {
        // do not support null when setBeautyEffectOptions to false
        rtcEngine().setBeautyEffectOptions(false, Constant.BEAUTY_OPTIONS);
    }


    protected void configEngine(VideoEncoderConfiguration.VideoDimensions videoDimension, VideoEncoderConfiguration.FRAME_RATE fps, String encryptionKey, String encryptionMode) {
        EncryptionConfig config = new EncryptionConfig();
        if (!TextUtils.isEmpty(encryptionKey)) {
            config.encryptionKey = encryptionKey;

            if (TextUtils.equals(encryptionMode, "AES-128-XTS")) {
                config.encryptionMode = EncryptionConfig.EncryptionMode.AES_128_XTS;
            } else if (TextUtils.equals(encryptionMode, "AES-256-XTS")) {
                config.encryptionMode = EncryptionConfig.EncryptionMode.AES_256_XTS;
            }
            rtcEngine().enableEncryption(true, config);
        } else {
            rtcEngine().enableEncryption(false, config);
        }

        Logger.d("configEngine " + videoDimension + " " + fps + " " + encryptionMode);
        // Set the Resolution, FPS. Bitrate and Orientation of the video
        rtcEngine().setVideoEncoderConfiguration(new VideoEncoderConfiguration(videoDimension,
                fps,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    protected RtcEngine rtcEngine() {
        return RtcManager.getInstance().getRtcEngine();
    }

    protected EngineConfig config() {
        return RtcManager.getInstance().getConfig();
    }

    protected void addEventHandler(AGEventHandler handler) {
        RtcManager.getInstance().addEventHandler(handler);
    }

    protected void removeEventHandler(AGEventHandler handler) {
        RtcManager.getInstance().remoteEventHandler(handler);
    }

}
