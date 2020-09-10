package com.xiaoyang.poweroperation.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.xiaoyang.poweroperation.data.basedate.BaseData;
import com.xiaoyang.poweroperation.data.entity.CodeInfoEntity;
import com.xiaoyang.poweroperation.data.entity.Device;
import com.xiaoyang.poweroperation.data.entity.DeviceType;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xiaoyang.poweroperation.device.contract.AddDeviceContract;
import com.xiaoyang.poweroperation.device.presenter.AddDevicePresenter;
import com.xiaoyang.poweroperation.di.component.DaggerAddDeviceComponent;
import com.xylib.base.utils.GsonUtils;
import com.xylib.base.utils.StringUtil;
import com.xylib.base.view.pickview.builder.OptionsPickerBuilder;
import com.xylib.base.view.pickview.builder.TimePickerBuilder;
import com.xylib.base.view.pickview.view.OptionsPickerView;
import com.xylib.base.view.pickview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AddDeviceActivity extends BaseActivity<AddDevicePresenter> implements AddDeviceContract.View {

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
    EditText tvDeviceCode;
    @BindView(R.id.tv_title_label)
    TextView tvTitleLabel;
    @BindView(R.id.edt_device_name)
    EditText edtDeviceName;
    @BindView(R.id.edt_model)
    EditText edtModel;
    @BindView(R.id.edt_longitude)
    EditText edtLongitude;
    @BindView(R.id.edt_latitude)
    EditText edtLatitude;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;
    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;
    @BindView(R.id.ll_device_type)
    LinearLayout llDeviceType;
    @BindView(R.id.tv_voltage_level)
    TextView tvVoltageLevel;
    @BindView(R.id.ll_voltage_level)
    LinearLayout llVoltageLevel;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.edt_remark)
    EditText edtRemark;
    @BindView(R.id.tv_create_user)
    TextView tvCreateUser;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private String mType;
    private String mRunStatus;
    private String deviceTypeJson;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAddDeviceComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_add_device; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("添加设备");
        mType = getIntent().getStringExtra("type");
        deviceTypeJson = getIntent().getStringExtra("deviceTypeJson");
        setStatus();
        setUser();
    }

    private void setStatus() {
        String deviceType = "";
        switch (mType) {
            case "1":
                deviceType = "变电站";
                break;
            case "2":
                deviceType = "开闭所";
                break;
            case "3":
                deviceType = "配电房";
                break;
            case "4":
                deviceType = "箱式变压器";
                break;

        }
        tvDeviceType.setText(deviceType);
    }

    private void setUser() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            tvCreateUser.setText(user.getNickname());
        }

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


    @OnClick({R.id.ll_device_type, R.id.ll_time, R.id.tv_submit, R.id.ll_status})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_status:
                List<String> statusStr = new ArrayList<>();
                List<CodeInfoEntity> mRunStatusList = BaseData.getRunStatusData();

                for (CodeInfoEntity code : mRunStatusList) {
                    statusStr.add(code.getKey());
                }
                OptionsPickerView typePickerView = new OptionsPickerBuilder(AddDeviceActivity.this, (options1, options2, options3, v) -> {
                    mRunStatus = statusStr.get(options1);
                    tvStatus.setText(mRunStatus);
                }).build();
                typePickerView.setPicker(statusStr);
                typePickerView.show();
                break;
            case R.id.ll_time:
                TimePickerView startTimePicker = new TimePickerBuilder(AddDeviceActivity.this, (date, v) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = sdf.format(date);
                    tvTime.setText(dateStr);
                }).setType(new boolean[]{true, true, true, false, false, false})
                        .build();
                startTimePicker.show();
                break;
            case R.id.tv_submit:
                String deviceName = edtDeviceName.getText().toString();
                String longitude = edtLongitude.getText().toString();
                String latitude = edtLatitude.getText().toString();
                String operationtime = tvTime.getText().toString();
                String remark = edtRemark.getText().toString();
                String voltagelevel = tvVoltageLevel.getText().toString();
                String raunstatus = tvStatus.getText().toString();
                if (StringUtil.isBlank(deviceName)) {
                    ArmsUtils.snackbarText("请输入设备名称");
                    return;
                }
                if (StringUtil.isBlank(longitude)) {
                    ArmsUtils.snackbarText("请输入设备经度");
                    return;
                }
                if (StringUtil.isBlank(latitude)) {
                    ArmsUtils.snackbarText("请输入设备纬度");
                    return;
                }

                if (StringUtil.isBlank(operationtime)) {
                    ArmsUtils.snackbarText("请输入投运时间");
                    return;
                }


                Device device = new Device();

                device.setDevice_name(deviceName);
                device.setLatitude(Double.valueOf(latitude));
                device.setLongitude(Double.valueOf(longitude));
                device.setYear(raunstatus);
                device.setVoltagelevel(voltagelevel);
                device.setDevice_type_id(mType);
                if (StringUtil.isNotBlank(remark)) {
                    device.setRemark(remark);
                }
                if (StringUtil.isNotBlank(deviceTypeJson)) {
                    device.setDevice_type(GsonUtils.fromJson(deviceTypeJson, DeviceType.class));
                }
                device.setCreateUser(BmobUser.getCurrentUser(User.class));
                device.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            ArmsUtils.snackbarText("添加设备成功");
                        } else {
                            ArmsUtils.snackbarText("添加设备失败");
                        }
                    }
                });
                break;
        }
    }
}
