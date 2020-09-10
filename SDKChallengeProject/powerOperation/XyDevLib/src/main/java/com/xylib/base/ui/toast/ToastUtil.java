package com.xylib.base.ui.toast;

import android.content.Context;
import android.content.DialogInterface;


/**
 * Created by Zyj on 2016/8/4.
 */
public class ToastUtil {

    private static LoadingDialog loadingDialog;

    public static void showToast(Context context, String icon, String text, int duration) {
        hideToast();
        LingJuToast.makeText(context, icon, text, duration, 1);
    }

    public static void showToast(Context context, int iconRes, int textRes, int duration) {
        hideToast();
        LingJuToast.makeText(context, iconRes, textRes, duration, 1);
    }

    public static void showToastLoading(Context context, int textRes) {
        String text = context.getResources().getString(textRes);
        showToastLoading(context, text);
    }

    public static void showToastLoading(Context context, String textRes) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    loadingDialog = null;
                }
            });
            try {
                loadingDialog.show();
            } catch (Exception e) {

            }
        }
        loadingDialog.setData(textRes);
    }

    public static void hideToast() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
