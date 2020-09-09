package com.xiaoyang.poweroperation.data.entity;

import java.io.Serializable;

/**
 * ProjectName: android_source
 * CreateDate: 2019-07-25
 * ClassName: LocationBean
 * Author: xiaoyangyan
 * note
 */
public class LocationBean implements Serializable {
    private String longitude;
    private String latitude;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
