package com.hwm.together.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwm.together.R;
import com.hwm.together.bean.FriendInfoBean;
import com.hwm.together.util.GlideUtil;
import com.hwm.together.util.OnItemClickCallback;
import com.hwm.together.view.activity.ShowUserActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder>{
    private List<FriendInfoBean> mData;
    private Context mContext;

    // 申明一个点击事件接口变量
    private OnItemClickCallback callback = null;

    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;

    public FriendsListAdapter(Context context, List<FriendInfoBean> list, OnItemClickCallback clickCallback) {
        this.mContext = context;
        this.mData = list;
        this.callback = clickCallback;
    }

    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_friends_list, parent, false);
        return new FriendsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (mData.get(position).getFriendNote() == null || TextUtils.equals("",mData.get(position).getFriendNote())) {
            holder.noteName.setText(mData.get(position).getUserAccount().getNickName());
        }else {
            holder.noteName.setText(mData.get(position).getFriendNote());
        }
        GlideUtil.loadCircleImg(mContext,mData.get(position).getUserAccount().getHeadImg(),holder.headImg);
        holder.tvBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, ShowUserActivity.class);
                mIntent.putExtra("account",mData.get(position).getUserAccount().getAccount());
                mIntent.putExtra("headImg",mData.get(position).getUserAccount().getHeadImg());
                mIntent.putExtra("nickName",mData.get(position).getUserAccount().getNickName());
                mIntent.putExtra("birthDay",mData.get(position).getUserAccount().getBirthDay());
                mIntent.putExtra("province",mData.get(position).getUserAccount().getProvince());
                mIntent.putExtra("city",mData.get(position).getUserAccount().getCity());
                mIntent.putExtra("sex",mData.get(position).getUserAccount().getSex());
                mIntent.putExtra("id",mData.get(position).getUserAccount().getId());
                mIntent.putExtra("friendTableId",mData.get(position).getId());
                mIntent.putExtra("noteName",mData.get(position).getFriendNote());
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headImg,tvBg;
        TextView noteName;

        public ViewHolder(View itemView) {
            super(itemView);
            headImg = itemView.findViewById(R.id.rv_friends_headImg);
            noteName = itemView.findViewById(R.id.rv_friends_noteName);
            tvBg = itemView.findViewById(R.id.rv_friends_bg);
        }
    }
}
