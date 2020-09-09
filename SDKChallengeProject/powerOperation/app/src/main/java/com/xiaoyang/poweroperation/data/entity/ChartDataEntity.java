package com.xiaoyang.poweroperation.data.entity;

import java.io.Serializable;

/**
 * ProjectName: powerOperation
 * CreateDate: 2020/8/26
 * ClassName: ChartDataEntity
 * Author: xiaoyangyan
 * note
 */
public class ChartDataEntity implements Serializable {
    private String id;
    private String label;
    private int num;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
