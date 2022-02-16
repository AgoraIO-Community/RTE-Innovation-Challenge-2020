package com.hwm.together.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwm.together.R;
import com.hwm.together.bean.FriendApplication;
import com.hwm.together.bean.FriendsApplyBean;
import com.hwm.together.eventbus.MessageEvent;
import com.hwm.together.internet.BaseResponse;
import com.hwm.together.internet.RetrofitUtil;
import com.hwm.together.util.GlideUtil;
import com.hwm.together.util.MyLogUtil;
import com.hwm.together.util.OnItemClickCallback;
import com.hwm.together.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFriendsAdapter extends RecyclerView.Adapter<NewFriendsAdapter.ViewHolder>{
    private static final String TAG = "NewFriendsAdapter";
    private List<FriendsApplyBean> mData;
    private Context mContext;

    // 申明一个点击事件接口变量
    private OnItemClickCallback callback = null;

    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;

    public NewFriendsAdapter(Context context, List<FriendsApplyBean> list, OnItemClickCallback clickCallback) {
        this.mContext = context;
        this.mData = list;
        this.callback = clickCallback;
    }

    @Override
    public NewFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_new_friends_list, parent, false);
        return new NewFriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewFriendsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.nickName.setText(mData.get(position).getUserAccount().getNickName());
        holder.tvVerify.setText(mData.get(position).getApplicationNote());
        GlideUtil.loadCircleImg(mContext,mData.get(position).getUserAccount().getHeadImg(),holder.headImg);
        String status = mData.get(position).getIfAgreement();
        switch (status) {
            case "0":
                break;
            case "1":
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnrefuse.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("已同意");
                break;
            case "2":
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnrefuse.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("已拒绝");
                break;
        }
        final FriendApplication friendApplication = new FriendApplication();
        friendApplication.setApplicantId(mData.get(position).getApplicantId());
        friendApplication.setRespondentId(mData.get(position).getRespondentId());
        friendApplication.setId(mData.get(position).getId());
        //拒绝
        holder.btnrefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendApplication.setIfAgreement("2");
                updateFriendStatus(friendApplication);
            }
        });
        //接受
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendApplication.setIfAgreement("1");
                updateFriendStatus(friendApplication);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headImg;
        TextView nickName,tvVerify,tvStatus;
        Button btnrefuse,btnAccept;

        public ViewHolder(View itemView) {
            super(itemView);
            headImg = itemView.findViewById(R.id.rv_new_friends_headImg);
            nickName = itemView.findViewById(R.id.rv_new_friends_nickName);
            tvVerify = itemView.findViewById(R.id.rv_new_friends_verify);
            tvStatus = itemView.findViewById(R.id.rv_new_friends_status);
            btnAccept = itemView.findViewById(R.id.rv_new_friends_btn_accept);
            btnrefuse = itemView.findViewById(R.id.rv_new_friends_btn_refuse);
        }
    }

    private void updateFriendStatus(FriendApplication friendApplication){
        Call<BaseResponse> call = RetrofitUtil.getInstance().getApiServer().updateFriendStatus(friendApplication);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                String code = response.body().getCode();
                String message = response.body().getMessage();
                if (TextUtils.equals("200",code)) {
                    ToastUtil.makeShort(mContext,"已通过好友申请");
                    EventBus.getDefault().post(new MessageEvent(1));
                    ((Activity)mContext).finish();
                }else {
                    MyLogUtil.i(TAG,code + message);
                    ToastUtil.makeShort(mContext,code + message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                ToastUtil.makeShort(mContext,"网络请求失败，请检测网络或稍后重试！");
                t.printStackTrace();
            }
        });
    }
}
