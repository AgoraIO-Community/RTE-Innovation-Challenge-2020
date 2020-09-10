package com.xiaoyang.poweroperation.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.account.contract.EdtPwdContract;
import com.xiaoyang.poweroperation.account.presenter.EdtPwdPresenter;
import com.xiaoyang.poweroperation.di.component.DaggerEdtPwdComponent;
import com.xylib.base.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * Package:
 * ClassName:      EdtPwdActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/18 17:37
 * Description:   修改密码
 */
public class EdtPwdActivity extends BaseActivity<EdtPwdPresenter> implements EdtPwdContract.View {

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_old_password)
    EditText edtOldPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerEdtPwdComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_edt_pwd; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setTitle("修改密码");
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


    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        String oldPassword = edtOldPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String tryPassword = edtConfirmPassword.getText().toString();


        if (StringUtil.isBlank(oldPassword)) {
            ArmsUtils.snackbarText("请输入旧密码");
            return;
        }

        if (StringUtil.isBlank(newPassword)) {
            ArmsUtils.snackbarText("请输入新密码");
            return;
        }
        if (StringUtil.isBlank(tryPassword)) {
            ArmsUtils.snackbarText("请输入确认密码");
            return;
        } else {
            if (!newPassword.equals(tryPassword)) {
                ArmsUtils.snackbarText("两次输入的密码不一致！");
                return;
            }
        }

    }
}
