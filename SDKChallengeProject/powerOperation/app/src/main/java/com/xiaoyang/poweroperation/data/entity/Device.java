package com.xiaoyang.poweroperation.data.entity;

import cn.bmob.v3.BmobObject;

/**
 * ProjectName: powerOperation
 * CreateDate: 2020/8/11
 * ClassName: Device
 * Author: xiaoyangyan
 * note
 */
public class Device extends BmobObject {
    private String device_name;
    private String device_type_id;
    private double latitude;
    private double longitude;
    private boolean runstatus;
    private DeviceType device_type;
    private int status;
    private String statusLabel;
    private String year;
    private User CreateUser;
    private String voltagelevel;
    private String remark;
    private String device_id;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVoltagelevel() {
        return voltagelevel;
    }

    public void setVoltagelevel(String voltagelevel) {
        this.voltagelevel = voltagelevel;
    }

    public User getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(User createUser) {
        CreateUser = createUser;
    }

    public String getStatusLabel() {
        switch (status) {
            case 1:
                statusLabel = "正常";
                break;
            case 2:
                statusLabel = "异常";
                break;
            case 3:
                statusLabel = "告警";
                break;
            case 4:
                statusLabel = "高危";
                break;


        }
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isRunstatus() {
        return runstatus;
    }

    public void setRunstatus(boolean runstatus) {
        this.runstatus = runstatus;
    }

    public DeviceType getDevice_type() {
        return device_type;
    }

    public void setDevice_type(DeviceType device_type) {
        this.device_type = device_type;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(String device_type_id) {
        this.device_type_id = device_type_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
