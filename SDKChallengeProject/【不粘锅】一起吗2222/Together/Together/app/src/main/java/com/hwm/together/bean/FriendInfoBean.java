package com.hwm.together.bean;

public class FriendInfoBean {
    private String id;
    private String userId;
    private String friendId;
    private String friendNote;
    private String createDate;
    private String updateTime;

    private UserInfo userAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendNote() {
        return friendNote;
    }

    public void setFriendNote(String friendNote) {
        this.friendNote = friendNote;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public UserInfo getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserInfo userAccount) {
        this.userAccount = userAccount;
    }
}
