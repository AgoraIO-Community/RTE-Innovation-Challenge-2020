package com.hwm.together.util;

import android.content.Context;
import android.widget.Toast;

/**
 * <pre>
 *     @author: hanweiming
 *     time: 2020/8/11
 *     desc:
 *     version: 1.0
 * </pre>
 */
public class ToastUtil {
    private static Toast toast;

    public static void makeShort(Context context,String text){
        if (toast == null) {
            toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void makeLong(Context context,String text){
        if (toast == null) {
            toast = Toast.makeText(context,text,Toast.LENGTH_LONG);
        }else {
            toast.setText(text);
        }
        toast.show();
    }

}
