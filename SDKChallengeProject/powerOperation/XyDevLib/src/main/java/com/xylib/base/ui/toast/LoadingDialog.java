package com.xylib.base.ui.toast;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xylib.base.R;
import com.xylib.base.utils.StringUtil;

/**
 * Created by Zyj on 2016/8/4.
 */
public class LoadingDialog extends Dialog {

    private Handler handler;
    private ImageView toastIconView;
    private TextView toastTextView;
    private RotateAnimation loadingAnimation;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog_style);
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_layout);
        toastIconView = (ImageView) findViewById(R.id.toast_icon);
        toastTextView = (TextView) findViewById(R.id.toast_text);
        loadingAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        loadingAnimation.setDuration(1000);
        loadingAnimation.setRepeatMode(Animation.RESTART);
        loadingAnimation.setRepeatCount(200);
        toastIconView.startAnimation(loadingAnimation);
        // 主背景模糊
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //alpha 在某些屏幕上无效
        params.dimAmount = 0F;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(false);
    }

    public void setToastText(String text) {
        if (StringUtil.isBlank(text)) {
            toastTextView.setVisibility(View.GONE);
            return;
        }
        toastTextView.setVisibility(View.VISIBLE);
        toastTextView.setText(text);
    }

    public void setToastText(int textRes) {
        if (textRes <= 0) {
            toastTextView.setVisibility(View.GONE);
            return;
        }
        toastTextView.setVisibility(View.VISIBLE);
        toastTextView.setText(textRes);
    }

    public void setData(String content) {
        setToastText(content);
    }

    public void setData(int content) {
        setToastText(content);
    }

    @Override
    public void dismiss() {
        loadingAnimation.cancel();
        toastIconView.clearAnimation();
        super.dismiss();
    }
}
