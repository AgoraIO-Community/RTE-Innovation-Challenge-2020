package com.xiaoyang.poweroperation.mine.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoyang.poweroperation.data.entity.QA;
import com.xiaoyang.poweroperation.mine.viewholder.QaViewHolder;

import java.util.List;

/**
 * Package:
 * ClassName:      SignRecordAdapter
 * Author:         xiaoyangyan
 * CreateDate:     2020/5/26 15:53
 * Description:
 */
public class QaAdapter extends BaseQuickAdapter<QA, QaViewHolder> {

    public QaAdapter(int layoutResId, @Nullable List<QA> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(QaViewHolder helper, QA item) {
        helper.setData(item);
    }


}