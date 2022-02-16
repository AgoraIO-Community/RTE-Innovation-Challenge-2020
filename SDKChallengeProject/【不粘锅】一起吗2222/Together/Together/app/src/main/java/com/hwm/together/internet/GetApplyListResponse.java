package com.hwm.together.internet;

import com.hwm.together.bean.FriendsApplyBean;

import java.util.List;

public class GetApplyListResponse {
    private String code;
    private String message;
    private List<FriendsApplyBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FriendsApplyBean> getData() {
        return data;
    }

    public void setData(List<FriendsApplyBean> data) {
        this.data = data;
    }
}
