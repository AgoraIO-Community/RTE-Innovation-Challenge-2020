package com.wy.pressurecrusher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.wy.pressurecrusher.utils.TitleBarUtil;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        TitleBarUtil.enableTranslucentStatus(getWindow());
    }
}
