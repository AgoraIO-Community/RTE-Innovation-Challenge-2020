package com.hwm.together.bean;

public class FriendApplication {
    //申请人id
    private String applicantId;
    //被申请人Id
    private String respondentId;
    //申请备注
    private String applicationNote;
    //创建时间
    private String createDate;
    //主键
    private String id;
    //是否同意申请: 0 申请中, 1 已同意, 2 拒绝
    private String ifAgreement;
    //更新时间
    private String updateTime;

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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIfAgreement() {
        return ifAgreement;
    }

    public void setIfAgreement(String ifAgreement) {
        this.ifAgreement = ifAgreement;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
