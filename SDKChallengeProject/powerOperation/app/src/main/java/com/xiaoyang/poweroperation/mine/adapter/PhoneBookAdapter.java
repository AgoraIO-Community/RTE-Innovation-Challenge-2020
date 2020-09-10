package com.xiaoyang.poweroperation.mine.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xiaoyang.poweroperation.mine.viewholder.PhoneBookViewHolder;

import java.util.List;

/**
 *
 * Package:
 * ClassName:      SignRecordAdapter
 * Author:         xiaoyangyan
 * CreateDate:     2020/5/26 15:53
 * Description:
 */
public class PhoneBookAdapter extends BaseQuickAdapter<User, PhoneBookViewHolder> {

    public PhoneBookAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(PhoneBookViewHolder helper, User item) {
        helper.setData(item);
    }


}