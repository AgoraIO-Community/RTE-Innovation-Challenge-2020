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

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    ImageButton ibBack,ibClear;
    TextView tvTitle;
    EditText etAccount,etPassword;
    CheckBox cbAgreement,cbShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    public void initView(){
        ibBack = findViewById(R.id.register_in_top_bar).findViewById(R.id.ib_navigation_back);
        tvTitle = findViewById(R.id.register_in_top_bar).findViewById(R.id.ib_navigation_title);
        ibClear = findViewById(R.id.img_register_clear);
        etAccount = findViewById(R.id.et_register_username);
        etPassword = findViewById(R.id.et_register_auth_code);
        cbAgreement = findViewById(R.id.cb_register_agreement);
        cbShowPassword = findViewById(R.id.register_cb_show_password);
        tvTitle.setText("注册");
        ibClear.setVisibility(View.GONE);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_activity_in_left, R.anim.slide_activity_out_right);
                finish();
            }
        });

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
//                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
//                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
    }

    public void register(View view){
        if (etAccount.getText() == null || TextUtils.equals("",etAccount.getText())) {
            ToastUtil.makeShort(RegisterActivity.this,getResources().getString(R.string.no_phone_number));
        } else if (etAccount.getText().length() <11) {
            ToastUtil.makeShort(RegisterActivity.this,getResources().getString(R.string.wrong_format_phone_number));
        }else if (etPassword.getText() == null || TextUtils.equals("",etPassword.getText())) {
            ToastUtil.makeShort(RegisterActivity.this,getResources().getString(R.string.no_password));
        }else if (etPassword.getText().length() < 6 || etPassword.getText().length() > 16 ) {
            ToastUtil.makeShort(RegisterActivity.this,getResources().getString(R.string.wrong_format_password));
        }else if (!cbAgreement.isChecked()) {
            ToastUtil.makeShort(RegisterActivity.this,getResources().getString(R.string.no_agreement));
        } else {
            sendRegister(String.valueOf(etAccount.getText()),String.valueOf(etPassword.getText()));
        }
    }

    public void clear(View view){
        etPassword.setText("");
    }

    private void sendRegister(String account,String passwd) {
        UserInfo registerInfo = new UserInfo();
        registerInfo.setAccount(account);
        registerInfo.setPasswd(passwd);
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().postRegister(registerInfo);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                String data = response.body().getData();
                if (TextUtils.equals("200",code)) {
                    ToastUtil.makeShort(RegisterActivity.this,"注册成功！");
                    MyApplication.mUserInfo.setAccount(etAccount.getText().toString());
                    Intent intent = new Intent(RegisterActivity.this,PersonalInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(RegisterActivity.this,code + message + data);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(RegisterActivity.this,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_activity_in_left, R.anim.slide_activity_out_right);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
