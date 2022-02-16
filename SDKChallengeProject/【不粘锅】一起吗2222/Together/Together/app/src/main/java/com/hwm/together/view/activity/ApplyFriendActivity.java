package com.hwm.together.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hwm.together.R;
import com.hwm.together.bean.FriendApplication;
import com.hwm.together.internet.BaseResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.SoftKeyboardUtil;
import com.hwm.together.util.StatusBarUtil;
import com.hwm.together.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyFriendActivity extends BaseActivity {
    private static final String TAG = "ApplyFriendActivity";
    private ImageButton ibBack;
    private TextView tvTitle,tvFriendNickName;
    private EditText etVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTranslucent(this);
        setContentView(R.layout.activity_apply_friend);

        initView();
    }

    private void initView(){
        ibBack = findViewById(R.id.apply_friend_in_top_bar).findViewById(R.id.include_title_black_back);
        tvTitle = findViewById(R.id.apply_friend_in_top_bar).findViewById(R.id.include_title_black_title);
        etVerify = findViewById(R.id.apply_friend_et_verify);
        tvFriendNickName = findViewById(R.id.apply_friend_tv_friendNickName);
        tvFriendNickName.setText(getIntent().getStringExtra("nickName"));

        tvTitle.setText("申请添加朋友");
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击空白隐藏软键盘
        View view = findViewById(R.id.apply_friend_cl_parentView);
        SoftKeyboardUtil.updateUI(this,view);
    }

    public void addFriend(View view){
        Intent intent = getIntent();
        FriendApplication friendApplication = new FriendApplication();
        friendApplication.setApplicantId(intent.getStringExtra("myId"));
        friendApplication.setRespondentId(intent.getStringExtra("friendId"));
        if (etVerify.getText() != null && !TextUtils.equals("",etVerify.getText().toString())){
            friendApplication.setApplicationNote(etVerify.getText().toString());
        }
        sendAddFriend(friendApplication);
    }

    private void sendAddFriend(FriendApplication friendApplication){
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().postAddFriend(friendApplication);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                if (TextUtils.equals("200",code)) {
                    ToastUtil.makeShort(ApplyFriendActivity.this,"申请已发送，请等待对方验证");
                    finish();
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(ApplyFriendActivity.this,code + message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(ApplyFriendActivity.this,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }
}