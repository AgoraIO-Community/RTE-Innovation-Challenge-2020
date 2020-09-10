package com.hwm.together.util;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftKeyboardUtil {

    //隐藏软键盘
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus()!=null){
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    //点击editText以外区域隐藏软键盘
    public static void updateUI(final Activity activity, View view){
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    SoftKeyboardUtil.hideSoftKeyboard(activity);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for(int i = 0; i < ((ViewGroup) view).getChildCount(); i++){
                View innerView = ((ViewGroup) view).getChildAt(i);
                updateUI(activity,innerView);
            }
        }
    }

}
