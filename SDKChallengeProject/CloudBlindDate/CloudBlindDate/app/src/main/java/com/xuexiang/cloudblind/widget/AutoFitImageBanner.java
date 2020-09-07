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

package com.xuexiang.cloudblind.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.strategy.DiskCacheStrategyEnum;
import com.xuexiang.xui.widget.imageview.strategy.LoadOption;

/**
 * @author xuexiang
 * @since 2020/8/27 11:23 PM
 */
class AutoFitImageBanner extends SimpleImageBanner {

    public AutoFitImageBanner(Context context) {
        super(context);
    }

    public AutoFitImageBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void loadingImageView(ImageView iv, BannerItem item) {
        String imgUrl = item.imgUrl;
        if (!TextUtils.isEmpty(imgUrl)) {
            if (mScale > 0) {
                int itemWidth = getItemWidth();
                int itemHeight = (int) (itemWidth * mScale);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

                LoadOption option = LoadOption.of(mPlaceHolder)
                        .setSize(itemWidth, itemHeight)
                        .setCacheStrategy(mEnableCache ? DiskCacheStrategyEnum.RESOURCE : DiskCacheStrategyEnum.NONE);
                ImageLoader.get().loadImage(iv, imgUrl, option);
            } else {
                ImageLoader.get().loadImage(iv, imgUrl, mPlaceHolder, mEnableCache ? DiskCacheStrategyEnum.RESOURCE : DiskCacheStrategyEnum.NONE);
            }
        } else {
            iv.setImageDrawable(mPlaceHolder);
        }
    }
}
