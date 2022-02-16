package com.hwm.together.internet;

import com.hwm.together.bean.FriendInfoBean;

import java.util.List;

public class GetFriendsListResponse {
    private String code;
    private String message;
    private List<FriendInfoBean> data;

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

    public List<FriendInfoBean> getData() {
        return data;
    }

    public void setData(List<FriendInfoBean> data) {
        this.data = data;
    }
}

