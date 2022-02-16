package com.hwm.together.view.activity;

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

import com.hwm.together.MyApplication;
import com.hwm.together.R;
import com.hwm.together.bean.UserInfo;
import com.hwm.together.internet.BaseResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends BaseActivity {
    private static final String TAG = "ForgetPasswordActivity";
    ImageButton ibBack,ibClear,againClear;
    TextView tvTitle;
    EditText etAccount,etPassword,againPassword,etNickname;
    CheckBox cbShowPassword,againShowPassword;
    private int exitType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Intent intent = getIntent();
        exitType = intent.getIntExtra("exitType",0);
        initView();
    }

    public void initView(){
        ibBack = findViewById(R.id.forget_in_top_bar).findViewById(R.id.ib_navigation_back);
        tvTitle = findViewById(R.id.forget_in_top_bar).findViewById(R.id.ib_navigation_title);
        ibClear = findViewById(R.id.img_register_clear);
        etAccount = findViewById(R.id.et_register_username);
        etPassword = findViewById(R.id.et_register_auth_code);
        cbShowPassword = findViewById(R.id.register_cb_show_password);
        againClear = findViewById(R.id.forget_img_clear);
        againPassword = findViewById(R.id.forget_et_auth_code);
        againShowPassword = findViewById(R.id.forget_cb_show_password);
        etNickname = findViewById(R.id.forget_et_nick);
        tvTitle.setText("重置密码");
        ibClear.setVisibility(View.GONE);
        againClear.setVisibility(View.GONE);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exitType != 1){
                    Intent intent = new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_activity_in_left, R.anim.slide_activity_out_right);
                }
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
                    //默认状态显示密码--设置文本
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        againShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    againPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    againPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

        againPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (againPassword.getText() == null || TextUtils.equals("",againPassword.getText())) {
                    againClear.setVisibility(View.GONE);
                }else {
                    againClear.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void clear(View view){
        etPassword.setText("");
    }

    public void againClear(View view){
        againPassword.setText("");
    }

    public void resetPassword(View view){
        if (etAccount.getText() == null || TextUtils.equals("",etAccount.getText())) {
            ToastUtil.makeShort(ForgetPasswordActivity.this,getResources().getString(R.string.no_phone_number));
        } else if (etAccount.getText().length() <11) {
            ToastUtil.makeShort(ForgetPasswordActivity.this,getResources().getString(R.string.wrong_format_phone_number));
        }else if (etPassword.getText() == null || TextUtils.equals("",etPassword.getText())) {
            ToastUtil.makeShort(ForgetPasswordActivity.this,getResources().getString(R.string.no_password));
        }else if (etPassword.getText().length() < 6 || etPassword.getText().length() > 16 ) {
            ToastUtil.makeShort(ForgetPasswordActivity.this,getResources().getString(R.string.wrong_format_password));
        }else if (!TextUtils.equals(etPassword.getText(),againPassword.getText())) {
            ToastUtil.makeShort(ForgetPasswordActivity.this,getResources().getString(R.string.password_not_same));
        }else if (etNickname.getText() == null || TextUtils.equals("",etNickname.getText())) {
            ToastUtil.makeShort(ForgetPasswordActivity.this,"请输入昵称");
        } else{
            sendReset(String.valueOf(etAccount.getText()),String.valueOf(etPassword.getText()),String.valueOf(etNickname.getText()));
        }
    }

    public void sendReset(String account,String newPasswd,String nickname){
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPasswd(newPasswd);
        userInfo.setNickName(nickname);
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().putResetPassword(userInfo);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                String data = response.body().getData();
                if (TextUtils.equals("200",code)) {
                    ToastUtil.makeShort(ForgetPasswordActivity.this,"密码重置成功,请重新登录！");
                    MyApplication.mUserInfo = null;
                    Intent intent = new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_activity_in_left, R.anim.slide_activity_out_right);
                    finish();
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(ForgetPasswordActivity.this,code + message + data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(ForgetPasswordActivity.this,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (exitType != 1){
                Intent intent = new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_activity_in_left, R.anim.slide_activity_out_right);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}