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

public class VideoInfoData {

    public int mWidth;
    public int mHeight;
    public int mDelay;
    public int mFrameRate;
    public int mBitRate;
    public int mCodec;

    public VideoInfoData(int width, int height, int delay, int frameRate, int bitRate, int codec) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDelay = delay;
        this.mFrameRate = frameRate;
        this.mBitRate = bitRate;
        this.mCodec = codec;
    }

    public VideoInfoData(int width, int height, int delay, int frameRate, int bitRate) {
        this(width, height, delay, frameRate, bitRate, 0);
    }

    @Override
    public String toString() {
        return "VideoInfoData{" +
                "mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                ", mDelay=" + mDelay +
                ", mFrameRate=" + mFrameRate +
                ", mBitRate=" + mBitRate +
                ", mCodec=" + mCodec +
                '}';
    }
}