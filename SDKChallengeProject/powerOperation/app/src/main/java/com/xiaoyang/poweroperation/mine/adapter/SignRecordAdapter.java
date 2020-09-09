package com.xiaoyang.poweroperation.mine.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoyang.poweroperation.data.entity.Message;
import com.xiaoyang.poweroperation.data.entity.SignRecord;
import com.xiaoyang.poweroperation.mine.viewholder.SignRecordViewHolder;

import java.util.List;

/**
 * Package:
 * ClassName:      DeviceViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020-02-06 22:22
 * Description:
 */
public class SignRecordAdapter extends BaseQuickAdapter<SignRecord, SignRecordViewHolder> {

    public SignRecordAdapter(int layoutResId, @Nullable List<SignRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(SignRecordViewHolder helper, SignRecord item) {
        helper.setData(item);
    }


}