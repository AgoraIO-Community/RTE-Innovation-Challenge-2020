package com.xiaoyang.poweroperation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.device.DeviceListActivity;
import com.xiaoyang.poweroperation.di.component.DaggerHomeFragmentComponent;
import com.xiaoyang.poweroperation.main.contract.HomeFragmentContract;
import com.xiaoyang.poweroperation.main.presenter.HomeFragmentPresenter;
import com.xiaoyang.poweroperation.map.LineActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * Package:
 * ClassName:      HomeFragment
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/9 22:50
 * Description:   首页
 */
public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements HomeFragmentContract.View {


    @BindView(R.id.tv_toolbar_title_tv)
    TextView tvToolbarTitleTv;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeFragmentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

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

    }


    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_1:
                intent = new Intent(getContext(), DeviceListActivity.class);
                intent.putExtra("device_type", "1");
                startActivity(intent);
                break;
            case R.id.tv_2:
                intent = new Intent(getContext(), DeviceListActivity.class);
                intent.putExtra("device_type", "2");
                startActivity(intent);
                break;
            case R.id.tv_3:
                intent = new Intent(getContext(), DeviceListActivity.class);
                intent.putExtra("device_type", "3");
                startActivity(intent);
                break;
            case R.id.tv_4:
                intent = new Intent(getContext(), DeviceListActivity.class);
                intent.putExtra("device_type", "4");
                startActivity(intent);
                break;
            case R.id.tv_5:
                intent = new Intent(getContext(), LineActivity.class);
                startActivity(intent);
                break;
        }
    }
}
