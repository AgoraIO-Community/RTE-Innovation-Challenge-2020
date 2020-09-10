package com.hwm.together.bean;

public class FriendsApplyBean {
    //表id
    private String id;
    //申请人Id
    private String applicantId;
    //被申请人Id
    private String respondentId;
    //申请验证消息
    private String applicationNote;
    //是否同意申请: 0 申请中, 1 已同意, 2 拒绝
    private String ifAgreement;
    private String createDate;
    private String updateTime;
    //申请者用户信息
    private UserInfo userAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getRespondentId() {
        return respondentId;
    }

    public void setRespondentId(String respondentId) {
        this.respondentId = respondentId;
    }

    public String getApplicationNote() {
        return applicationNote;
    }

    public void setApplicationNote(String applicationNote) {
        this.applicationNote = applicationNote;
    }

    public String getIfAgreement() {
        return ifAgreement;
    }

    public void setIfAgreement(String ifAgreement) {
        this.ifAgreement = ifAgreement;
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
