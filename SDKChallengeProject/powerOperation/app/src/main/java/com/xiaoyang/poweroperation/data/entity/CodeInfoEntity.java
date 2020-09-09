package com.xiaoyang.poweroperation.data.entity;

import java.io.Serializable;

/**
 * ProjectName: PowerService
 * CreateDate: 2020/3/17
 * ClassName: CodeInfoEntity
 * Author: xiaoyangyan
 * note
 */
public class CodeInfoEntity implements Serializable {
    private String key;
    private String value;
    private  int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
