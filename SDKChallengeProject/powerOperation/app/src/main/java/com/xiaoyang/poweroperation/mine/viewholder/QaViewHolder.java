package com.xiaoyang.poweroperation.mine.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.QA;
import com.xylib.base.widgets.CollapseView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Package:
 * ClassName:      PhoneBookViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020/5/27 15:14
 * Description:
 */
public class QaViewHolder extends BaseViewHolder {

    @BindView(R.id.collapseView1)
    CollapseView collapseView1;

    public QaViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }


    public void setData(QA info) {
        collapseView1.setTitle(info.getTitle());
        collapseView1.setContent(info.getContent());
    }

}
