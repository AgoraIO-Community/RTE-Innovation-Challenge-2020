/*
 * Date: 14-11-21
 * Project: Access-Control-TV
 */
package com.wy.pressurecrusher.base;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 进行简单封装的列表适配器

 * https://www.cnblogs.com/zly1022/p/7755076.html
 */
public class BaseListAdapter<T, H extends BaseListAdapter.ViewHolder> extends BaseAdapter {
    private final List<T> mData;
    private final ViewCreator<T, H> mViewCreator;

    public BaseListAdapter(ViewCreator<T, H > creator) {
        this(new ArrayList<T>(), creator);
    }

    public BaseListAdapter(List<T> data, ViewCreator<T, H> creator) {
        mData = data == null ? new ArrayList<T>() : data;
        mViewCreator = creator;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final H holder;
        if (convertView == null) {
            holder = mViewCreator.createHolder(position, parent);
            convertView = holder.itemView;
        } else {
            holder = (H) convertView.getTag();
        }
        mViewCreator.bindData(position, holder, getItem(position));
        return convertView;
    }

    public void update(List<T> data) {
        mData.clear();
        addData(data);
    }

    public void addData(List<T> data) {
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(T data) {
        if (data != null) {
            mData.add(data);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        if(mData!=null){
            mData.clear();
        }
        notifyDataSetChanged();
    }

    public static abstract class ViewHolder {
        public final View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            itemView.setTag(this);
        }
    }

    public static class DefaultViewHolder extends ViewHolder {
        private SparseArray<View> mHolderViews;

        public DefaultViewHolder(View view) {
            super(view);
            mHolderViews = new SparseArray<>();
        }

        public void hold(int... resIds) {
            for (int id : resIds) {
                mHolderViews.put(id, itemView.findViewById(id));
            }
        }

        public <V> V get(int id) {
            return (V) mHolderViews.get(id);
        }
    }
}
