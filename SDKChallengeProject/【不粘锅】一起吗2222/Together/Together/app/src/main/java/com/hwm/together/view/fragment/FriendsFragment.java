package com.hwm.together.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwm.together.MyApplication;
import com.hwm.together.R;
import com.hwm.together.adapter.FriendsListAdapter;
import com.hwm.together.bean.FriendInfoBean;
import com.hwm.together.eventbus.MessageEvent;
import com.hwm.together.internet.GetFriendsListResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.internet.UserInfoResponse;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.OnItemClickCallback;
import com.hwm.together.util.SoftKeyboardUtil;
import com.hwm.together.util.ToastUtil;
import com.hwm.together.view.activity.NewFriendsActivity;
import com.hwm.together.view.activity.ShowUserActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 好友
 */
public class FriendsFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "FriendsFragment";
    private RecyclerView mRecyclerView;
    List<FriendInfoBean> mdata = new ArrayList<>();
    private EditText etSearchAccount;
    private TextView tvCancelSearch,tvSubmitSearch,tvGoApply;
    private ImageView imgSearch,imgRedPoint;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        initView(view);
        if(!EventBus.getDefault().isRegistered(this)){
            //注册事件
            EventBus.getDefault().register(this);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        if(!EventBus.getDefault().isRegistered(this)){
//            //注册事件
//            EventBus.getDefault().register(this);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
//        if (EventBus.getDefault().isRegistered(this)){
//            //注销事件
//            EventBus.getDefault().unregister(this);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            //注销事件
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view){
        mRecyclerView = view.findViewById(R.id.friends_fg_rv_friends_list);
        etSearchAccount = view.findViewById(R.id.friends_fg_et_search);
        tvCancelSearch = view.findViewById(R.id.friends_fg_tv_cancel_search);
        tvSubmitSearch = view.findViewById(R.id.friends_fg_tv_submit_search);
        imgSearch = view.findViewById(R.id.friends_fg_img_search);
        tvGoApply = view.findViewById(R.id.friends_fg_tv_new_friends);
        imgRedPoint = view.findViewById(R.id.friends_fg_img_red_point);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSearchAccount.getVisibility() == View.GONE) {
                    imgSearch.setVisibility(View.GONE);
                    etSearchAccount.setVisibility(View.VISIBLE);
                    tvCancelSearch.setVisibility(View.VISIBLE);
                    tvSubmitSearch.setVisibility(View.VISIBLE);
                }
            }
        });
        tvSubmitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSearchAccount.getText() == null || TextUtils.equals("",etSearchAccount.getText().toString())) {
                    ToastUtil.makeShort(getActivity(),"请输入帐号");
                }else {
                    sendSearch(etSearchAccount.getText().toString());
                }
            }
        });
        tvCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearchAccount.setVisibility(View.GONE);
                etSearchAccount.clearFocus();
                etSearchAccount.setText("");
                tvCancelSearch.setVisibility(View.GONE);
                tvSubmitSearch.setVisibility(View.GONE);
                imgSearch.setVisibility(View.VISIBLE);
            }
        });
        tvGoApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgRedPoint.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), NewFriendsActivity.class);
                startActivity(intent);
            }
        });
        SoftKeyboardUtil.updateUI(getActivity(),view);
    }

    private void sendGetFriendsList(String userId){
        if (MyApplication.mUserInfo == null && MyApplication.mUserInfo.getId() == null) {
            return;
        }
        Call<GetFriendsListResponse> call = RetrofitUtil.getInstance().getApiServer().getFriendsList(userId);
        call.enqueue(new Callback<GetFriendsListResponse>() {
            @Override
            public void onResponse(Call<GetFriendsListResponse> call, Response<GetFriendsListResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                if (TextUtils.equals("200",code)) {
                    //TODO
                    if (response.body().getData() != null) {
                        MyApplication.mFriendsList = response.body().getData();
                        setFriendsAdapter(MyApplication.mFriendsList);
                    }

                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(getActivity(),code + message);
                }
            }

            @Override
            public void onFailure(Call<GetFriendsListResponse> call, Throwable t) {
                ToastUtil.makeShort(getActivity(),"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }

    private void sendSearch(String searchAccount){
        Call<UserInfoResponse> call = RetrofitUtil.getInstance().getApiServer().getUserInfo(searchAccount);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                if (TextUtils.equals("200",code)) {
                    Intent mIntent = new Intent(getActivity(), ShowUserActivity.class);
                    mIntent.putExtra("account",response.body().getData().getAccount());
                    mIntent.putExtra("headImg",response.body().getData().getHeadImg());
                    mIntent.putExtra("nickName",response.body().getData().getNickName());
                    mIntent.putExtra("birthDay",response.body().getData().getBirthDay());
                    mIntent.putExtra("province",response.body().getData().getProvince());
                    mIntent.putExtra("city",response.body().getData().getCity());
                    mIntent.putExtra("sex",response.body().getData().getSex());
                    mIntent.putExtra("id",response.body().getData().getId());

                    startActivity(mIntent);

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

    //创建Adapter并传递数据
    private void setFriendsAdapter(List<FriendInfoBean> mdata){
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(getActivity(), mdata, new OnItemClickCallback() {
            @Override
            public void onClick(View view, Object info) {
                //Toast.makeText(ApplicationUtil.getInstance(),info.toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, Object info) {
            }
        });
        mRecyclerView.setAdapter(friendsListAdapter);
//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(26,20));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager) ;
    }

    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        switch (messageEvent.getType()){//1-
            case 0:
                break;
            case 1:
                sendGetFriendsList(MyApplication.mUserInfo.getId());
                break;
            case 2:
                imgRedPoint.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
