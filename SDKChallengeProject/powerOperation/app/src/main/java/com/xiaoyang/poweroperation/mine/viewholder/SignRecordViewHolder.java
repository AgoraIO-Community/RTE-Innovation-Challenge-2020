package com.xiaoyang.poweroperation.mine.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.SignRecord;
import com.xiaoyang.poweroperation.data.entity.SignType;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Package:
 * ClassName:      DeviceTypeViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020/2/26 15:09
 * Description:
 */
public class SignRecordViewHolder extends BaseViewHolder {


    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;

    public SignRecordViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }


    public void setData(SignRecord info) {
        SignType type = info.getSignType();

        if (type != null) {
            Log.v("yxy","===>"+type.getType());
            tvName.setText("签到类型：" + type.getType());
        }

        tvContent.setText(info.getLocation());
        tvTime.setText(info.getCreatedAt());
    }

}
