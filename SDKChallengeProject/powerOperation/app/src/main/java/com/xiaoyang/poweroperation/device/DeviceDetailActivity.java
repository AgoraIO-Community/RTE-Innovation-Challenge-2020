package com.xiaoyang.poweroperation.device;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.Device;
import com.xiaoyang.poweroperation.device.contract.DeviceDetailContract;
import com.xiaoyang.poweroperation.device.presenter.DeviceDetailPresenter;
import com.xiaoyang.poweroperation.di.component.DaggerDeviceDetailComponent;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * Package:
 * ClassName:      DeviceDetailActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/11 13:17
 * Description:   设备详情
 */
public class DeviceDetailActivity extends BaseActivity<DeviceDetailPresenter> implements DeviceDetailContract.View {


    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.tv_toolbar_title_tv)
    TextView tvToolbarTitleTv;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.tv_device_code)
    TextView tvDeviceCode;
    @BindView(R.id.tv_title_label)
    TextView tvTitleLabel;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    private String deviceId;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDeviceDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_device_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        deviceId = getIntent().getStringExtra("device_id");
        tvRight.setVisibility(View.VISIBLE);
        tvToolbarTitleTv.setText("设备详情");
        tvRight.setText("反馈");
        getDeviceById();
    }

    /**
     * name为football的类别
     */
    private void getDeviceById() {
        BmobQuery<Device> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("device_id", deviceId);
        categoryBmobQuery.findObjects(new FindListener<Device>() {
            @Override
            public void done(List<Device> object, BmobException e) {
                if (e == null) {
                    setDeviceData(object.get(0));
                } else {
                }
            }
        });
    }

    private void setDeviceData(Device deviceInfo) {
        tvDeviceName.setText(deviceInfo.getDevice_name());
        switch (deviceInfo.getStatus()) {
            case 1:
                tvStatus.setTextColor(Color.parseColor("#009B78"));
                break;
            case 2:
                tvStatus.setTextColor(Color.parseColor("#FBC752"));
                break;
            case 3:
                tvStatus.setTextColor(Color.parseColor("#FE8743"));
                break;
            case 4:
                tvStatus.setTextColor(Color.parseColor("#FF6767"));
                break;
        }
        tvStatus.setText(deviceInfo.getStatusLabel());
        tvDeviceCode.setText(deviceInfo.getObjectId());
        tvLevel.setText(deviceInfo.getVoltagelevel());
        tvTime.setText(deviceInfo.getYear());
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


    @OnClick(R.id.tv_right)
    public void onViewClicked() {
        Intent intent = new Intent(this, CheckFeedBackActivity.class);
        intent.putExtra("id", deviceId);
        startActivity(intent);
    }
}
