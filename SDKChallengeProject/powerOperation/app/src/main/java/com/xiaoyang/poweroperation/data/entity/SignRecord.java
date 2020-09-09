package com.xiaoyang.poweroperation.data.entity;

import cn.bmob.v3.BmobObject;

/**
 * ProjectName: powerOperation
 * CreateDate: 2020/8/18
 * ClassName: SignReocrd
 * Author: xiaoyangyan
 * note
 */
public class SignRecord extends BmobObject {
    private String signTime;
    private String location;
    private User user;
    private String signTypeId;
    private SignType signType;
    private double temperature;
    private boolean healthstatus;
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isHealthstatus() {
        return healthstatus;
    }

    public void setHealthstatus(boolean healthstatus) {
        this.healthstatus = healthstatus;
    }

    public String getSignTypeId() {
        return signTypeId;
    }

    public void setSignTypeId(String signTypeId) {
        this.signTypeId = signTypeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SignType getSignType() {
        return signType;
    }

    public void setSignType(SignType signType) {
        this.signType = signType;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
