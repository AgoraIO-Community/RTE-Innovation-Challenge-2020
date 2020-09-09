package com.xiaoyang.poweroperation.data.entity;

import cn.bmob.v3.BmobObject;

/**
 * ProjectName: powerOperation
 * CreateDate: 2020/8/18
 * ClassName: Message
 * Author: xiaoyangyan
 * note
 */
public class Message extends BmobObject {
    private String title;
    private String message_type;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
