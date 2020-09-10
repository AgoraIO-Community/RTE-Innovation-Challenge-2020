package com.xiaoyang.poweroperation.device;

import android.content.Intent;
import android.os.Bundle;
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
import com.xiaoyang.poweroperation.device.contract.CheckFeedBackContract;
import com.xiaoyang.poweroperation.device.presenter.CheckFeedBackPresenter;
import com.xiaoyang.poweroperation.di.component.DaggerCheckFeedBackComponent;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * Package:
 * ClassName:      CheckFeedBackActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/28 12:50
 * Description:
 */
public class CheckFeedBackActivity extends BaseActivity<CheckFeedBackPresenter> implements CheckFeedBackContract.View {

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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.edt_model)
    EditText edtModel;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;
    @BindView(R.id.edt_remark)
    EditText edtRemark;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCheckFeedBackComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_check_feed_back; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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
