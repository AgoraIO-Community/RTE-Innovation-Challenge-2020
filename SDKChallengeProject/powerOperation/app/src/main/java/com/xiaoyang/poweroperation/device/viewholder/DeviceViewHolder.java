package com.xiaoyang.poweroperation.device.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.Device;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Package:
 * ClassName:      DeviceTypeViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020/2/26 15:09
 * Description:
 */
public class DeviceViewHolder extends BaseViewHolder {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_vol)
    TextView tvVol;
    @BindView(R.id.tv_level)
    TextView tvLevel;


    public DeviceViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }


    public void setData(Device info) {
        tvName.setText(info.getDevice_name());
        switch (info.getStatus()) {
            case 1:
                tvStatus.setTextColor(Color.parseColor("#009B78"));
                break;
            case 2:
                tvStatus.setTextColor(Color.parseColor("#FBC752"));
                break;
            case 3:
                tvStatus.setTextColor(Color.parseColor("#FE8743"));
                break;
            case 4:
                tvStatus.setTextColor(Color.parseColor("#FF6767"));
                break;
        }
        tvStatus.setText(info.getStatusLabel());

        tvYear.setText("出厂年份：" + info.getYear());
        tvVol.setText("电压等级" + info.getVoltagelevel());
    }

}
