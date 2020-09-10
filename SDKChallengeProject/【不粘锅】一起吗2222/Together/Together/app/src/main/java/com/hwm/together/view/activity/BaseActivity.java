package com.hwm.together.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hwm.together.MyApplication;
import com.hwm.together.util.StatusBarUtil;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucent(this);
        MyApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(null!= this.getCurrentFocus()){
//            //点击空白位置 隐藏软键盘
//            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
//        }
//        return super.onTouchEvent(event);
//    }
}
