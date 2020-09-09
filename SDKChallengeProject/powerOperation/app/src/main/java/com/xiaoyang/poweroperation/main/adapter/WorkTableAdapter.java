package com.xiaoyang.poweroperation.main.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoyang.poweroperation.data.entity.WorkTableEntity;
import com.xiaoyang.poweroperation.main.viewholder.WorkTableViewHolder;

import java.util.List;

/**
 * Package:
 * ClassName:      MessageListAdapter
 * Author:         xiaoyangyan
 * CreateDate:     2020-02-06 22:22
 * Description:
 */
public class WorkTableAdapter extends BaseQuickAdapter<WorkTableEntity, WorkTableViewHolder> {

    public WorkTableAdapter(int layoutResId, @Nullable List<WorkTableEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(WorkTableViewHolder helper, WorkTableEntity item) {
        helper.setData(item);
    }

}