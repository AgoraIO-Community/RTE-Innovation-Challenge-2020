package com.xiaoyang.poweroperation.mine.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Package:
 * ClassName:      DeviceTypeViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020/2/26 15:09
 * Description:
 */
public class MessageViewHolder extends BaseViewHolder {


    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;

    public MessageViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }


    public void setData(Message info) {
        tvTitle.setText(info.getTitle());
        tvContent.setText(info.getContent());
        tvTime.setText(info.getCreatedAt());
    }

}
