package com.xylib.base.ui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xylib.base.R;
import com.xylib.base.utils.StringUtil;


/**
 * 自定义Toast
 */
public class LingJuToast {

    public static void makeText(Context context, String icon, String msg, int length, int type) {
        Toast toast = new Toast(context);
        switch (type) {
            case 1: {
                View layout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null, false);
                ImageView toastIconView = (ImageView) layout.findViewById(R.id.toast_icon);
                TextView toastTextView = (TextView) layout.findViewById(R.id.toast_text);
                if (StringUtil.isBlank(icon)) {
                    toastIconView.setVisibility(View.GONE);
                }
                toastTextView.setText(msg);
                toast.setView(layout);
                break;
            }
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(length);
        toast.show();
    }

    public static void makeText(Context context, int iconId, int msgId, int length, int type) {
        String icon = context.getResources().getString(iconId);
        String msg = context.getResources().getString(msgId);
        makeText(context, icon, msg, length, type);
    }
}
