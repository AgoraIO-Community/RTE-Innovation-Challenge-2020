package com.xiaoyang.poweroperation.mine.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Package:
 * ClassName:      PhoneBookViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020/5/27 15:14
 * Description:
 */
public class PhoneBookViewHolder extends BaseViewHolder {

    @BindView(R.id.img_record)
    ImageView imgRecord;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_job)
    TextView tvJob;

    public PhoneBookViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }


    public void setData(User info) {
        tvName.setText("姓名：" + info.getNickname());
        tvPhone.setText(info.getMobilePhoneNumber());
        tvCompany.setText("单位：" + info.getCompany());
        tvJob.setText("职位：" + info.getJob());


    }

}
