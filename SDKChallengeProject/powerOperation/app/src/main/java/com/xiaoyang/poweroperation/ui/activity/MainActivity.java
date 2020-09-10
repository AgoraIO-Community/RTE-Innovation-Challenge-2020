package com.xiaoyang.poweroperation.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.Constants;
import com.xiaoyang.poweroperation.di.component.DaggerMainComponent;
import com.xiaoyang.poweroperation.main.ChartFragment;
import com.xiaoyang.poweroperation.main.HomeFragment;
import com.xiaoyang.poweroperation.main.MineFragment;
import com.xiaoyang.poweroperation.main.WorkTabFragment;
import com.xiaoyang.poweroperation.ui.contract.MainContract;
import com.xiaoyang.poweroperation.ui.presenter.MainPresenter;
import com.xylib.base.utils.StatusBarUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * Package:
 * ClassName:      MainActivity
 * Author:         xiaoyangyan
 * CreateDate:     2019-12-12 16:18
 * Description:   主页
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    private static final int REQUEST_PERMISSION_LOCATION = 2020;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.fragment_group)
    FrameLayout fragmentGroup;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;


    private ArrayList<BaseFragment> mFragments;
    private HomeFragment homeFragment;
    private MineFragment mineFragment;
    private ChartFragment chartFragment;
    private WorkTabFragment workTabFragment;
    private int mLastFgIndex;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initStatusBar();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            }

        }
        mFragments = new ArrayList<>();
        initFragments();
        mBottomNavigationView.setSelectedItemId(R.id.tab_home);
        initPager(true, Constants.TAB_HOME);
        initBottomNavigationView();
//        sHA1(this);
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            Log.v("yxy", "sHA1==>" + result.substring(0, result.length() - 1));

            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initPager(boolean isRecreate, int position) {
        homeFragment = HomeFragment.newInstance();
        mFragments.add(homeFragment);
        initFragments();
        initBottomNavigationView();
        switchFragment(position);
    }

    private void initStatusBar() {
        int color = getResources().getColor(R.color.white);
        StatusBarUtil.setColor(this, color, 0);
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setPadding(MainActivity.this, fragmentGroup);
    }

    private void initBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.tab_home:
                    loadPager(getString(R.string.bottom_home), 0,
                            homeFragment, Constants.TAB_HOME);
                    break;
                case R.id.tab_chart:
                    loadPager(getString(R.string.bottom_chart), 1,
                            chartFragment, Constants.TAB_CHART);
                    break;
//                case R.id.tab_work:
//                    loadPager(getString(R.string.bottom_work), 2,
//                            workTabFragment, Constants.TAB_WORK);
//                    break;
                case R.id.tab_mine:
                    loadPager(getString(R.string.bottom_mine), 2,
                            mineFragment, Constants.TAB_MINE);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 切换fragment
     *
     * @param position 要显示的fragment的下标
     */
    private void switchFragment(int position) {
        if (position >= mFragments.size()) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment targetFg = mFragments.get(position);
        Fragment lastFg = mFragments.get(mLastFgIndex);
        mLastFgIndex = position;
        ft.hide(lastFg);
        if (!targetFg.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(targetFg).commitAllowingStateLoss();
            ft.add(R.id.fragment_group, targetFg);
        }
        ft.show(targetFg);
        ft.commitAllowingStateLoss();
    }

    private void loadPager(String title, int position, BaseFragment mFragment, int pagerType) {
        switchFragment(position);
    }


    private void initFragments() {
        homeFragment = HomeFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        chartFragment = ChartFragment.newInstance();
//        workTabFragment=WorkTabFragment.newInstance();
        mFragments.add(homeFragment);
        mFragments.add(chartFragment);
//        mFragments.add(workTabFragment);
        mFragments.add(mineFragment);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
