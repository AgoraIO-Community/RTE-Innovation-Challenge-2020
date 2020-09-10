package com.hwm.together.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwm.together.R;
import com.hwm.together.chase.Chase;
import com.hwm.together.util.OnItemClickCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChaseAdapter extends RecyclerView.Adapter<ChaseAdapter.ViewHolder>{
    private List<Chase> mData;
    private Context mContext;

    // 申明一个点击事件接口变量
    private OnItemClickCallback callback = null;

    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;

    public ChaseAdapter(Context context, List<Chase> list, OnItemClickCallback clickCallback) {
        this.mContext = context;
        this.mData = list;
        this.callback = clickCallback;
    }

    @Override
    public ChaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_chase, parent, false);
        return new ChaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChaseAdapter.ViewHolder holder, final int position) {
        holder.chaseName.setText(mData.get(position).getName());
        Glide.with(mContext).load(mData.get(position).getCover()).into(holder.coverImg);
        /*
        * 选择框
        * 给item中的指定控件添加点击事件（可以是item的子控件，也可以是itemView本身）
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(v,mData.get(position));
            }
        });
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    map.clear();
                    map.put(position, true);
                    checkedPosition = position;
                } else {
                    map.remove(position);
                    if (map.size() == 0) {
                        checkedPosition = -1; //-1 代表一个都未选择
                    }
                }
                if (!onBind) {
                    notifyDataSetChanged();
                }
            }
        });
        onBind = true;
        if (map != null && map.containsKey(position)) {
            holder.mCheckBox.setChecked(true);
        } else {
            holder.mCheckBox.setChecked(false);
        }
        onBind = false;

         */
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImg;
        TextView chaseName;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.rv_item_chase_img);
            chaseName = itemView.findViewById(R.id.rv_item_chase_name);
        }
    }
}
