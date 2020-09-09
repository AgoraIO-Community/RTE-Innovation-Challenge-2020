package com.xiaoyang.poweroperation.device.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoyang.poweroperation.data.entity.Device;
import com.xiaoyang.poweroperation.device.viewholder.DeviceViewHolder;

import java.util.List;

/**
 * Package:
 * ClassName:      DeviceViewHolder
 * Author:         xiaoyangyan
 * CreateDate:     2020-02-06 22:22
 * Description:
 */
public class DeviceAdapter extends BaseQuickAdapter<Device, DeviceViewHolder> {

    private String mCode;

    public DeviceAdapter(int layoutResId, @Nullable List<Device> data) {
        super(layoutResId, data);
    }

    public void setCode(String code) {
        mCode = code;
    }

    @Override
    protected void convert(DeviceViewHolder helper, Device item) {
        helper.setData(item);
    }


}