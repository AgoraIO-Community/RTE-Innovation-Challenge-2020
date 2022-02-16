package com.hwm.together.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwm.together.MyApplication;
import com.hwm.together.R;
import com.hwm.together.bean.FriendInfoBean;
import com.hwm.together.bean.FriendsBean;
import com.hwm.together.eventbus.MessageEvent;
import com.hwm.together.internet.BaseResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.util.GlideUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.StatusBarUtil;
import com.hwm.together.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowUserActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ShowUserActivity";
    private ImageButton ibBack;
    private TextView tvTitle,tvNoteName,tvNickName,tvGender,tvBirthday,tvLoaction;
    private TextView tvAddFriend,tvUpdateNote;
    private FriendsBean mFriendsBean;
    private ImageView imgHead;
    private EditText inputNoteName;
    private String frinendTableId;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private View layout;
    private TextView tvMakeSure;
    private TextView tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTranslucent(this);
        setContentView(R.layout.activity_show_user);

        getPassIntent();
        initView();
    }

    private void getPassIntent(){
        Intent intent = getIntent();
        mFriendsBean = new FriendsBean();
        mFriendsBean.setAccount(intent.getStringExtra("account"));
        mFriendsBean.setBirthDay(intent.getStringExtra("birthDay"));
        mFriendsBean.setHeadImg(intent.getStringExtra("headImg"));
        mFriendsBean.setNickName(intent.getStringExtra("nickName"));
        mFriendsBean.setSex(intent.getStringExtra("sex"));
        mFriendsBean.setProvince(intent.getStringExtra("province"));
        mFriendsBean.setCity(intent.getStringExtra("city"));
        mFriendsBean.setId(intent.getStringExtra("id"));
        mFriendsBean.setNoteName(intent.getStringExtra("noteName"));

        frinendTableId = intent.getStringExtra("friendTableId");

    }

    private void initView(){
        ibBack = findViewById(R.id.show_user_in_top_bar).findViewById(R.id.include_title_black_back);
        tvTitle = findViewById(R.id.show_user_in_top_bar).findViewById(R.id.include_title_black_title);

        imgHead = findViewById(R.id.show_user_headImg);
        tvNoteName = findViewById(R.id.show_user_tv_noteName);
        tvNickName = findViewById(R.id.show_user_tv_nickName);
        tvGender = findViewById(R.id.show_user_tv_get_gender);
        tvBirthday = findViewById(R.id.show_user_tv_get_birthday);
        tvLoaction = findViewById(R.id.show_user_tv_get_location);
        tvAddFriend = findViewById(R.id.show_user_tv_addFriends);
        tvUpdateNote = findViewById(R.id.show_user_tv_updateNoteName);
        inputNoteName = findViewById(R.id.show_user_et_inputNoteName);

        GlideUtil.loadCircleImg(this,mFriendsBean.getHeadImg(),imgHead);
        tvNickName.setText("昵称：" + mFriendsBean.getNickName());
        switch (mFriendsBean.getSex()) {
            case "0":
                tvGender.setText("女");
                break;
            case "1":
                tvGender.setText("男");
                break;
            case "2":
                break;
        }
        if (mFriendsBean.getBirthDay() != null) {
            tvBirthday.setText(mFriendsBean.getBirthDay().substring(0,10));
        }
        if (!TextUtils.equals("",mFriendsBean.getProvince())){
            tvLoaction.setText(mFriendsBean.getProvince() +" " + mFriendsBean.getCity());
        }
        if (mFriendsBean.getNoteName() == null || TextUtils.equals("",mFriendsBean.getNoteName())) {
            tvNoteName.setText(mFriendsBean.getNickName());
        }else {
            tvNoteName.setText(mFriendsBean.getNoteName());
        }

        tvTitle.setText("资料查看");
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //添加好友、删除好友、修改备注
        if (isMyFriend()) {
            tvAddFriend.setText("删除好友");
            tvUpdateNote.setVisibility(View.VISIBLE);
        }

    }

    public void addFriends(View view){
        if (isMyFriend()) {
            //是好友
            if (inputNoteName.getVisibility() == View.GONE) {
                //不在编辑状态---删除好友
                showMakeSureDialog();
            }else {
                //在编辑状态---确认修改
                FriendInfoBean friendInfoBean = new FriendInfoBean();
                friendInfoBean.setFriendId(mFriendsBean.getId());
                friendInfoBean.setUserId(MyApplication.mUserInfo.getId());
                friendInfoBean.setId(frinendTableId);
                friendInfoBean.setFriendNote(inputNoteName.getText().toString());
                sendUpdateNote(friendInfoBean);
            }
        }else {
            Intent intent = new Intent(this,ApplyFriendActivity.class);
            intent.putExtra("myId",MyApplication.mUserInfo.getId());
            intent.putExtra("friendId",mFriendsBean.getId());
            intent.putExtra("nickName",mFriendsBean.getNickName());
            startActivity(intent);
        }
    }

    public void updateNoteName(View view){
        if (inputNoteName.getVisibility() == View.GONE) {
            //不在编辑状态
            inputNoteName.setVisibility(View.VISIBLE);
            inputNoteName.setFocusable(true);
            inputNoteName.setFocusableInTouchMode(true);
            inputNoteName.requestFocus();
            tvAddFriend.setText("确认修改");
            tvUpdateNote.setText("取消修改");
        }else {
            inputNoteName.setVisibility(View.GONE);
            tvAddFriend.setText("删除好友");
            tvUpdateNote.setText("修改备注");
        }
    }

    private void sendDeleteFriend(FriendInfoBean friendInfoBean){
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().deleteFriend(friendInfoBean);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                if (TextUtils.equals("200",code)) {
                    EventBus.getDefault().post(new MessageEvent(1));
                    ToastUtil.makeShort(ShowUserActivity.this,"删除好友成功");
                    finish();
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(ShowUserActivity.this,code + message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(ShowUserActivity.this,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }

    private void sendUpdateNote(FriendInfoBean friendInfoBean){
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().updateFriendNoteName(friendInfoBean);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                if (TextUtils.equals("200",code)) {
                    EventBus.getDefault().post(new MessageEvent(1));
                    ToastUtil.makeShort(ShowUserActivity.this,"备注修改成功");
                    finish();
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(ShowUserActivity.this,code + message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(ShowUserActivity.this,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }

    public void showMakeSureDialog() {
        builder = new AlertDialog.Builder(this);//创建对话框
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_make_sure, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框

        tvMakeSure = layout.findViewById(R.id.dialog_make_sure_submit);
        tvCancel = layout.findViewById(R.id.dialog_make_sure_cancel);
        //设置监听
        tvMakeSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private boolean isMyFriend(){
        if (MyApplication.mFriendsList != null) {
            for (FriendInfoBean friendInfoBean : MyApplication.mFriendsList) {
                String account = friendInfoBean.getUserAccount().getAccount();
                if (TextUtils.equals(account,mFriendsBean.getAccount())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_make_sure_submit:
                FriendInfoBean friendInfoBean = new FriendInfoBean();
                friendInfoBean.setFriendId(mFriendsBean.getId());
                friendInfoBean.setUserId(MyApplication.mUserInfo.getId());
                friendInfoBean.setId(frinendTableId);
                sendDeleteFriend(friendInfoBean);
                dialog.dismiss();
                break;
            case R.id.dialog_make_sure_cancel:
                dialog.dismiss();//关闭对话框
                break;
        }
    }
}