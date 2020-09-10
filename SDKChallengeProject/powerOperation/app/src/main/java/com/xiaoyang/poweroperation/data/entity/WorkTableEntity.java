package com.xiaoyang.poweroperation.data.entity;

import java.io.Serializable;

/**
 * ProjectName: wangli
 * CreateDate: 2020-02-10
 * ClassName: WorkTableEntity
 * Author: xiaoyangyan
 * note
 */
public class WorkTableEntity implements Serializable {
    private String title;
    private int img;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
