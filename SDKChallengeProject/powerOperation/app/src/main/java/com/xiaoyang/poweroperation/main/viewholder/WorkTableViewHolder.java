package com.xiaoyang.poweroperation.main.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.WorkTableEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName: android_source
 * CreateDate: 2019-09-03
 * ClassName: DeviceViewHolder
 * Author: xiaoyangyan
 * note
 */
public class WorkTableViewHolder extends BaseViewHolder {
    @BindView(R.id.img_pic)
    ImageView imgPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public WorkTableViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }


    public void setData(WorkTableEntity info) {
        imgPic.setImageResource(info.getImg());
        tvTitle.setText(info.getTitle());
    }

}
