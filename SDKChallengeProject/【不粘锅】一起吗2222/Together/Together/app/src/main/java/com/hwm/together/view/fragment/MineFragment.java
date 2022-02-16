package com.hwm.together.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwm.together.MyApplication;
import com.hwm.together.R;
import com.hwm.together.bean.UserInfo;
import com.hwm.together.eventbus.MessageEvent;
import com.hwm.together.eventbus.PickerMessageEvent;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.internet.UserInfoResponse;
import com.hwm.together.util.GlideUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.ToastUtil;
import com.hwm.together.view.activity.ForgetPasswordActivity;
import com.hwm.together.view.activity.PersonalInfoActivity;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的
 */
public class MineFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "MineFragment";
    private TextView goPersonal,resetPasswd,tvNickname,tvGender,tvBirthday,tvLocation,setUp;
    public static boolean refreshUserInfo = false;
    private ImageView headImg;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        getUserInfo();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshUserInfo) {
            getUserInfo();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    private void initView(View view){
        goPersonal = view.findViewById(R.id.mine_fragment_tv_head);
        resetPasswd = view.findViewById(R.id.mine_fg_tv_reset_passwd);
        tvNickname = view.findViewById(R.id.mine_fragment_tv_head);
        tvGender = view.findViewById(R.id.mine_fg_tv_get_gender);
        tvBirthday = view.findViewById(R.id.mine_fg_tv_get_birthday);
        tvLocation = view.findViewById(R.id.mine_fg_tv_get_location);
        headImg = view.findViewById(R.id.mine_fg_headImg);
        setUp = view.findViewById(R.id.mine_fg_tv_setup);

        goPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PersonalInfoActivity.class);
                startActivity(intent);
            }
        });

        resetPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
                intent.putExtra("exitType",1);
                startActivity(intent);
            }
        });

        setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                testGeTui();


            }
        });
    }

    private void setUserInfo(UserInfo userInfo){
        MyApplication.mUserInfo = userInfo;
        GlideUtil.loadCircleImg(getActivity(),userInfo.getHeadImg(),headImg);
        tvNickname.setText(userInfo.getNickName());
        switch (userInfo.getSex()) {
            case "0":
                tvGender.setText("女");
                break;
            case "1":
                tvGender.setText("男");
                break;
            case "2":
                tvGender.setText("待完善");
                break;
        }
        if (userInfo.getBirthDay() != null) {
            tvBirthday.setText(userInfo.getBirthDay().substring(0,10));
        }if (!TextUtils.equals("",userInfo.getProvince())){
            tvLocation.setText(userInfo.getProvince() +" " + userInfo.getCity());
        }
    }

    private void getUserInfo(){
        if (MyApplication.mUserInfo == null) {
            return;
        }
        Call<UserInfoResponse> call = RetrofitUtil.getInstance().getApiServer().getUserInfo(MyApplication.mUserInfo.getAccount());
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                if (TextUtils.equals("200",code)) {
                    setUserInfo(response.body().getData());
                    EventBus.getDefault().post(new MessageEvent(1));
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(getActivity(),code + message);
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                ToastUtil.makeShort(getActivity(),"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }

}
