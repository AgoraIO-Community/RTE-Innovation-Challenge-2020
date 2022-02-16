package com.hwm.together.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hwm.together.MyApplication;
import com.hwm.together.util.StatusBarUtil;

public abstract class BaseFragmentActivity extends FragmentActivity {
    public static Activity act = null;
    protected final String TAG=getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act=this;
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTranslucent(this);
        setContentView(getLayoutID());
        initView();
        initData();
        initListener();
        MyApplication.getInstance().addActivity(this);
    }
    @LayoutRes
    protected abstract int getLayoutID();
    protected abstract void initListener();
    protected abstract void initView();
    protected abstract void initData();

    @SuppressWarnings("unchecked")
    protected <E> E f(int id){
        return (E)findViewById(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        DensityUtil.setCustomDensity(this,ApplicationUtil.getInstance(),0);
        //        initView();
        //        initData();
        //        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

}
