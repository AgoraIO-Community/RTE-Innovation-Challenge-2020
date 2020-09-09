package com.xiaoyang.poweroperation.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.account.EdtPwdActivity;
import com.xiaoyang.poweroperation.account.LoginActivity;
import com.xiaoyang.poweroperation.di.component.DaggerSettingComponent;
import com.xiaoyang.poweroperation.mine.contract.SettingContract;
import com.xiaoyang.poweroperation.mine.presenter.SettingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * Package:
 * ClassName:      SettingActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/12 13:55
 * Description:  设置
 */
public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View {


    @BindView(R.id.rl_edt_password)
    RelativeLayout rlEdtPassword;
    @BindView(R.id.rl_profile)
    RelativeLayout rlProfile;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.img_cache_right)
    ImageView imgCacheRight;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.tv_version)
    TextView tvVersion;
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

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSettingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_setting; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("设置");
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


    @OnClick({R.id.rl_edt_password, R.id.rl_clear_cache, R.id.tv_logout})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_edt_password:
                intent = new Intent(this, EdtPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_clear_cache:
                break;
            case R.id.tv_logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
