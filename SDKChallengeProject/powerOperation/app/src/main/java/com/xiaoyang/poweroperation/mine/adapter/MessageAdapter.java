package com.xiaoyang.poweroperation.mine.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoyang.poweroperation.data.entity.Message;
import com.xiaoyang.poweroperation.mine.viewholder.MessageViewHolder;

import java.util.List;

/**
 * Package:
 * ClassName:      DeviceViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020-02-06 22:22
 * Description:
 */
public class MessageAdapter extends BaseQuickAdapter<Message, MessageViewHolder> {

    public MessageAdapter(int layoutResId, @Nullable List<Message> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(MessageViewHolder helper, Message item) {
        helper.setData(item);
    }


}