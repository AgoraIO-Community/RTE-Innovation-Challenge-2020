package com.xiaoyang.poweroperation.device;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.DeviceType;
import com.xiaoyang.poweroperation.device.adapter.DeviceTypeAdapter;
import com.xiaoyang.poweroperation.device.contract.DeviceTypeContract;
import com.xiaoyang.poweroperation.device.presenter.DeviceTypePresenter;
import com.xiaoyang.poweroperation.di.component.DaggerDeviceTypeComponent;
import com.xylib.base.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DeviceTypeActivity extends BaseActivity<DeviceTypePresenter> implements DeviceTypeContract.View {

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_device_type)
    RecyclerView rlDeviceType;
    private List<DeviceType> mDataList;
    private DeviceTypeAdapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDeviceTypeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_device_type; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setText("设备类型");
        initDeviceTypeRecycleView();
        getDeviceType();
    }

    /**
     * 初始化设备类型
     */
    private void initDeviceTypeRecycleView() {
        mDataList = new ArrayList<>();
        mAdapter = new DeviceTypeAdapter(R.layout.item_device_type_view, mDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            DeviceType type = mDataList.get(position);
            Intent intent = new Intent(this, DeviceListActivity.class);
            startActivity(intent);

        });
        rlDeviceType.setAdapter(mAdapter);
        rlDeviceType.setLayoutManager(new LinearLayoutManager(Utils.getApp()));
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

    /**
     * 获取设备类型
     */
    private void getDeviceType() {
        BmobQuery<DeviceType> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<DeviceType>() {
            @Override
            public void done(List<DeviceType> object, BmobException e) {
                if (e == null) {
                    Log.v("yxy", "查询成功" + object.size());
                    mDataList = object;
                    mAdapter.replaceData(object);
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });
    }
}
