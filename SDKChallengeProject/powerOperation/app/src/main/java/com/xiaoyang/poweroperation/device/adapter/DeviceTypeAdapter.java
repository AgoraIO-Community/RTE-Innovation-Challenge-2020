package com.xiaoyang.poweroperation.device.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoyang.poweroperation.data.entity.DeviceType;
import com.xiaoyang.poweroperation.device.viewholder.DeviceTypeViewHolder;

import java.util.List;

/**
 * Package:
 * ClassName:      DeviceTypeViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020-02-06 22:22
 * Description:
 */
public class DeviceTypeAdapter extends BaseQuickAdapter<DeviceType, DeviceTypeViewHolder> {

    public DeviceTypeAdapter(int layoutResId, @Nullable List<DeviceType> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(DeviceTypeViewHolder helper, DeviceType item) {
        helper.setData(item);
    }


}