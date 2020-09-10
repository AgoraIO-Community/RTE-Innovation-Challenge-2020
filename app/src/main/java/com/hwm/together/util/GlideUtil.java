package com.hwm.together.util;

import android.content.Context;
import android.widget.ImageView;
import com.hwm.together.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtil {
    /**
     * 加载普通圆形图片（头像）
     *
     * @param mContext 上下文
     * @param url 图片url链接
     * @param mImageView ImageView控件
     */
    public static void loadCircleImg(Context mContext, String url, ImageView mImageView) {
        if (mContext == null || mImageView == null) {
            return;
        }
        RequestOptions options = RequestOptions
                .circleCropTransform()
                .placeholder(R.drawable.default_head_img)
                .error(R.drawable.default_head_img)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(mImageView);
    }

    public static void loadDontTransform(Context mContext, String url, ImageView mImageView,int errorImg){
        if (mContext == null || mImageView == null) {
            return;
        }
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(errorImg)
                .error(errorImg)
                .dontTransform()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(mImageView);
    }
}
