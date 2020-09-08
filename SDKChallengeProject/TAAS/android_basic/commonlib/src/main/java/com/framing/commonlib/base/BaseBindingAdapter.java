package com.framing.commonlib.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
 * Des
 * Author VULCAN
 * Date 2019/8/21
 */
public abstract class BaseBindingAdapter<DB extends ViewDataBinding,T>   extends RecyclerView.Adapter  {

    protected List<T> dataList;

    public Context context;
    private int layoutId;

    public BaseBindingAdapter(Context context, int layoutId){
        this.layoutId=layoutId;
        this.context = context;
    }
    public BaseBindingAdapter(Context context, List<T> dataList, int layoutId) {
        this.dataList = dataList;
        this.context = context;
        this.layoutId=layoutId;
    }
    public List<T> getDataList() {
        return dataList;
    }
    public void setDataList(List<T> dataLists) {
        if(dataList==null) {
            this.dataList = dataLists;
            this.notifyDataSetChanged();
        }else{
            DiffUtil.DiffResult result= DiffUtil.calculateDiff(new DiffCallBack<T>(dataList,dataLists),true);
            result.dispatchUpdatesTo(this);
            this.dataList = dataLists;
        }
    }

    // 抽象函数  获取布局资源id
    public int getLayoutId(){
        return layoutId;
    };

    // 抽象函数  通过databinding为布局设置数据
    public abstract void bindView(CommonViewHolder viewHolder,int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 生成DB对象 (这个方法是不是和View.inflate()很像？)
        DB bindView= DataBindingUtil.inflate(LayoutInflater.from(context),getLayoutId(),parent,false);
        return new CommonViewHolder(bindView);
    }

    @Override
    public  void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        // 调用抽象函数，将holder强转为CommonViewHodler，供子类Adapter使用其成员对象bindView；
        bindView((CommonViewHolder) holder,position);
    }



    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder{

        public DB bindView;
        // 每一个item都必须持有的一个ViewDataBinding子类对象

        public CommonViewHolder(DB bindView) {
            super(bindView.getRoot());
            this.bindView=bindView;
        }
    }
    class DiffCallBack<T> extends  DiffUtil.Callback{

        private List<T> mOldDatas, mNewDatas;//看名字


        public DiffCallBack(List<T> mOldDatas, List<T> mNewDatas) {
            this.mOldDatas = mOldDatas;
            this.mNewDatas = mNewDatas;
        }

        @Override
        public int getOldListSize() {
            return mOldDatas != null ? mOldDatas.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return mNewDatas != null ? mNewDatas.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldDatas.get(oldItemPosition).hashCode()==mNewDatas.get(newItemPosition).hashCode();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldDatas.get(oldItemPosition).hashCode()==mNewDatas.get(newItemPosition).hashCode();
        }
    }

}