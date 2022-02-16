package com.hwm.together.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hwm.together.MyApplication;
import com.hwm.together.R;
import com.hwm.together.eventbus.MessageEvent;
import com.hwm.together.util.ToastUtil;
import com.hwm.together.util.navigation.NavigationViewPager;
import com.hwm.together.util.navigation.PagerMainAdapter;
import com.hwm.together.view.fragment.HomepageFragment;
import com.hwm.together.view.fragment.FriendsFragment;
import com.hwm.together.view.fragment.MineFragment;
import com.hwm.together.view.fragment.ChaseFragment;
import com.hwm.together.view.fragment.CommunityFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class NavigationFragmentActivity extends BaseFragmentActivity {
    NavigationViewPager vp;
    private RadioGroup rg;
    private ImageView imgRedPoint;
    private int[] rbs = {R.id.navigation_homepage_rb, R.id.navigation_chase_rb, R.id.navigation_community_rb,R.id.navigation_friends_rb,R.id.navigation_mine_rb};
    private List<Fragment> mFragments;

    private static int checkedIndex = 0;
    private long exitTime = 0;
    //当前页面
    private boolean isFriendPage = false;

    public static void setIndex(int index){
        checkedIndex = index;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.igexin.sdk.PushManager.getInstance().initialize(this);
        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    private void checkLogin(){
        //未登录
        if (MyApplication.mUserInfo == null) {
            ToastUtil.makeShort(this,"请重新登录");
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            //注册事件
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)){
            //注销事件
            EventBus.getDefault().unregister(this);
        }
        //checkedIndex = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    //处理选择器发送结果事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        switch (messageEvent.getType()){//1-性别，2-行业，3-现居地，4-日期
            case 0:
                break;
            case 1:
                break;
            case 2:
                Log.i(TAG, "onMoonEvent: 当前index" + isFriendPage);
                if (!isFriendPage) {
                    imgRedPoint.setVisibility(View.VISIBLE);
                }
            default:
                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void initView() {
        vp = (NavigationViewPager)f(R.id.homepages_viewPager);
        rg = f(R.id.navigation_rg);
        imgRedPoint = f(R.id.navigation_mine_red_point);
    }
    @Override
    protected void initData() {

        mFragments=new ArrayList<>();
        HomepageFragment homepageFragment =new HomepageFragment();
        ChaseFragment chaseFragment =new ChaseFragment();
        CommunityFragment communityFragment = new CommunityFragment();
        FriendsFragment friendsFragment=new FriendsFragment();
        MineFragment mineFragment = new MineFragment();

        mFragments.add(homepageFragment);
        mFragments.add(chaseFragment);
        mFragments.add(communityFragment);
        mFragments.add(friendsFragment);
        mFragments.add(mineFragment);

        // 设置填充器
        vp.setAdapter(new PagerMainAdapter(getSupportFragmentManager(),mFragments));

        vp.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {

            }
        });//VerticalPageTransformer()
        vp.setOverScrollMode(View.OVER_SCROLL_NEVER);
        // 设置缓存页面数
        vp.setOffscreenPageLimit(5);

    }

    @Override
    protected void initListener() {
        //radioGroup的点击事件
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i < rbs.length; i++) {
                    if (rbs[i] != checkedId) continue;
                    //加载滑动
                    vp.setCurrentItem(i);
                    if (i == 3) {
                        isFriendPage = true;
                        imgRedPoint.setVisibility(View.GONE);
                    }
                }
            }
        });
        //ViewPager的点击事件 vp-rg互相监听：vp
        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                rg.check(rbs[position]);
            }
        });
        //设置一个默认页
        rg.check(rbs[checkedIndex]);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), getString(R.string.click_again_to_exit), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MyApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
