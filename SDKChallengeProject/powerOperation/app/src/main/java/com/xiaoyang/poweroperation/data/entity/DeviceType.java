package com.xiaoyang.poweroperation.data.entity;

import cn.bmob.v3.BmobObject;

/**
 * ProjectName: powerOperation
 * CreateDate: 2020/8/11
 * ClassName: DeviceType
 * Author: xiaoyangyan
 * note
 */
public class DeviceType extends BmobObject {
    private String type_name;
    private String type_id;

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
