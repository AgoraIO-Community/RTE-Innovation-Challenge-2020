package com.xiaoyang.poweroperation.device.viewholder;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.DeviceType;
import com.xylib.base.widgets.font.IconTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Package:
 * ClassName:      DeviceTypeViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020/2/26 15:09
 * Description:
 */
public class DeviceTypeViewHolder extends BaseViewHolder {


    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.img_type)
    IconTextView imgType;

    public DeviceTypeViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }


    public void setData(DeviceType info) {
        tvType.setText(info.getType_name());
        tvNum.setText(String.valueOf(info.getType_id()));
//        switch (info.getEquipcode()){
//            case "0":
//                imgType.setText(R.string.icon_biandainzhan);
//                break;
//            case "1":
//                imgType.setText(R.string.icon_kaibisuo);
//                break;
//            case "2":
//                imgType.setText(R.string.icon_huanwangxiang);
//                break;
//            case "3":
//                imgType.setText(R.string.icon_bianyaqi);
//                break;
//            case "4":
//                imgType.setText(R.string.icon_line);
//                break;
//        }

    }

}
