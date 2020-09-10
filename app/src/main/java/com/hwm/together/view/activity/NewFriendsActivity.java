package com.hwm.together.view.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hwm.together.MyApplication;
import com.hwm.together.R;
import com.hwm.together.adapter.NewFriendsAdapter;
import com.hwm.together.bean.FriendsApplyBean;
import com.hwm.together.internet.GetApplyListResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.OnItemClickCallback;
import com.hwm.together.util.StatusBarUtil;
import com.hwm.together.util.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFriendsActivity extends BaseActivity {
    private static final String TAG = "NewFriendsActivity";
    private ImageButton ibBack;
    private TextView tvTitle;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTranslucent(this);
        setContentView(R.layout.activity_new_friends);

        initView();
        getApplyList();
    }

    private void initView(){
        ibBack = findViewById(R.id.new_friends_in_top_bar).findViewById(R.id.include_title_black_back);
        tvTitle = findViewById(R.id.new_friends_in_top_bar).findViewById(R.id.include_title_black_title);

        mRecyclerView = findViewById(R.id.new_friends_rv_newList);

        tvTitle.setText("新的朋友");
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getApplyList(){
        if (MyApplication.mUserInfo == null && MyApplication.mUserInfo.getId() == null) {
            return;
        }
        Call<GetApplyListResponse> call = RetrofitUtil.getInstance().getApiServer().getApplyList(MyApplication.mUserInfo.getId());
        call.enqueue(new Callback<GetApplyListResponse>() {
            @Override
            public void onResponse(Call<GetApplyListResponse> call, Response<GetApplyListResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                if (TextUtils.equals("200",code)) {
                    //TODO
                    if (response.body().getData() != null) {
                        setFriendsAdapter(response.body().getData());
                    }

                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(NewFriendsActivity.this,code + message);
                }
            }

            @Override
            public void onFailure(Call<GetApplyListResponse> call, Throwable t) {
                ToastUtil.makeShort(NewFriendsActivity.this,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }

    //创建Adapter并传递数据
    private void setFriendsAdapter(List<FriendsApplyBean> mdata){
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        NewFriendsAdapter friendsListAdapter = new NewFriendsAdapter(this, mdata, new OnItemClickCallback() {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager) ;
    }
}