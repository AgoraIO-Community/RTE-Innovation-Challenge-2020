package org.daigua.crushonmeetings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "class")
public class ClassModel {

    public ClassModel() {
    }

    @Id
    @Column(name = "rid")
    private String rid;

    @Column(name = "time")
    private String time;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "student")
    private String student;

    @Column(name = "rname")
    private String rname;

    @Column(name = "video")
    private String video;

    @Column(name = "queryword")
    private String queryword;

    @Column(name = "cloudpng")
    private String cloudpng;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getQueryword() {
        return queryword;
    }

    public void setQueryword(String queryword) {
        this.queryword = queryword;
    }

    public String getCloudpng() {
        return cloudpng;
    }

    public void setCloudpng(String cloudpng) {
        this.cloudpng = cloudpng;
    }
}
