package com.xiaoyang.poweroperation.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.account.contract.LoginContract;
import com.xiaoyang.poweroperation.account.presenter.LoginPresenter;
import com.xiaoyang.poweroperation.account.session.UserSessionManager;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xiaoyang.poweroperation.di.component.DaggerLoginComponent;
import com.xiaoyang.poweroperation.ui.activity.MainActivity;
import com.xylib.base.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * Package:
 * ClassName:      LoginActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/9 22:01
 * Description:   登录
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;
    @BindView(R.id.rl_login)
    RelativeLayout rlLogin;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.img_edt_pwd)
    ImageView imgEdtPwd;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        edtUsername.setText("admin");
        edtPwd.setText("admin");
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


    @OnClick({R.id.rl_login, R.id.img_edt_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_login:
                String username = edtUsername.getText().toString().trim();
                if (StringUtil.isBlank(username)) {
                    ArmsUtils.snackbarText("请输入用户名");
                    return;
                }
                String password = edtPwd.getText().toString().trim();
                if (StringUtil.isBlank(password)) {
                    ArmsUtils.snackbarText("请输入密码");
                    return;
                }
                //此处替换为你的用户名密码
                BmobUser.loginByAccount("admin", "admin", new LogInListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Log.v("yxy", "" + "登录成功：" + user.getUsername());
                            ArmsUtils.snackbarText("登录成功：" + user.getUsername());
                            UserSessionManager.getInstance().setUserInfo(user);
                            ArmsUtils.startActivity(MainActivity.class);
                        } else {
                            Log.v("yxy", "" + "登录失败：" + e.getMessage());
                            ArmsUtils.snackbarText("登录失败：" + e.getMessage());
                        }
                    }
                });
                break;
            case R.id.img_edt_pwd:
                ArmsUtils.startActivity(EdtPwdActivity.class);
                break;
        }
    }
}
