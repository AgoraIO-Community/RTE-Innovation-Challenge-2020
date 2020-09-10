package com.hwm.together.eventbus;

public class MessageEvent {
    private int type;//0-不更新，1-更新好友信息,2-显示红点

    public MessageEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
