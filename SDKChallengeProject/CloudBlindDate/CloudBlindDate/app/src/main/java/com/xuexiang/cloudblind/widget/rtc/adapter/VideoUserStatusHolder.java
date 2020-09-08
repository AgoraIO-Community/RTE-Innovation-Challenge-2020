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

package com.xuexiang.cloudblind.widget.rtc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cloudblind.R;

public class VideoUserStatusHolder extends RecyclerView.ViewHolder {
    public final RelativeLayout mMaskView;

    public final ImageView mAvatar;
    public final ImageView mIndicator;

    public final LinearLayout mVideoInfo;

    public final TextView mMetaData;

    public VideoUserStatusHolder(View v) {
        super(v);

        mMaskView = (RelativeLayout) v.findViewById(R.id.user_control_mask);
        mAvatar = (ImageView) v.findViewById(R.id.default_avatar);
        mIndicator = (ImageView) v.findViewById(R.id.indicator);

        mVideoInfo = (LinearLayout) v.findViewById(R.id.video_info_container);

        mMetaData = (TextView) v.findViewById(R.id.video_info_metadata);
    }
}
