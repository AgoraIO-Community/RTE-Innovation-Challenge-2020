package com.hwm.together.view.activity;

import com.hwm.together.MyApplication;
import com.hwm.together.R;
import com.hwm.together.bean.UserInfo;
import com.hwm.together.internet.BaseResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.ToastUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    ImageButton ibBack,ibClear;
    TextView tvTitle,tvForget;
    EditText etAccount,etPassword;
    CheckBox cbShowPassword;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){
        ibBack = findViewById(R.id.login_in_top_bar).findViewById(R.id.ib_navigation_back);
        tvTitle = findViewById(R.id.login_in_top_bar).findViewById(R.id.ib_navigation_title);
        ibClear = findViewById(R.id.img_register_clear);
        etAccount = findViewById(R.id.et_register_username);
        etPassword = findViewById(R.id.et_register_auth_code);
        cbShowPassword = findViewById(R.id.register_cb_show_password);
        tvForget = findViewById(R.id.login_tv_forget);
        tvTitle.setText(getResources().getString(R.string.login));
        ibClear.setVisibility(View.GONE);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (etPassword.getText() == null || TextUtils.equals("",etPassword.getText())) {
                    ibClear.setVisibility(View.GONE);
                }else {
                    ibClear.setVisibility(View.VISIBLE);
                }
            }
        });

//        tvForget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public void clear(View view){
        etPassword.setText("");
    }

    //跳转忘记密码
    public void goForgetActivity(View view){
        Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_activity_in_right, R.anim.slide_activity_out_left);
    }

    //跳转注册
    public void goRegisterActivity(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_activity_in_right, R.anim.slide_activity_out_left);
    }

    public void login(View view){
        if (etAccount.getText() == null || TextUtils.equals("",etAccount.getText())) {
            ToastUtil.makeShort(LoginActivity.this,getResources().getString(R.string.no_phone_number));
        } else if (etAccount.getText().length() <11) {
            ToastUtil.makeShort(LoginActivity.this,getResources().getString(R.string.wrong_format_phone_number));
        }else if (etPassword.getText() == null || TextUtils.equals("",etPassword.getText())) {
            ToastUtil.makeShort(LoginActivity.this,getResources().getString(R.string.no_password));
        } else if (etPassword.getText().length() < 6 || etPassword.getText().length() > 16 ) {
            ToastUtil.makeShort(LoginActivity.this,getResources().getString(R.string.wrong_format_password));
        } else {
            sendLogin(String.valueOf(etAccount.getText()),String.valueOf(etPassword.getText()));
        }
    }

    private void sendLogin(String account,String passwd){
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPasswd(passwd);
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().postLogin(userInfo);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                String data = response.body().getData();
                if (TextUtils.equals("200",code)) {
                    ToastUtil.makeShort(LoginActivity.this,"登录成功！");
                    MyApplication.newUserInfo();
                    MyApplication.mUserInfo.setAccount(etAccount.getText().toString());
                    Intent intent = new Intent(LoginActivity.this,NavigationFragmentActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(LoginActivity.this,code + message + data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(LoginActivity.this,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
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