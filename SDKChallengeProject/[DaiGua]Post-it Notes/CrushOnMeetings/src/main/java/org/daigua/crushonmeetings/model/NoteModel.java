package org.daigua.crushonmeetings.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(NotePK.class)
@Table(name = "note")
public class NoteModel implements Serializable {

    private static final long serialVersionUID = -4466904221026481006L;

    public NoteModel() {
    }

    @Id
    @Column(name = "uid")
    private String uid;

    @Id
    @Column(name = "rid")
    private String rid;

    @Id
    @Column(name = "time")
    private String time;

    @Column(name = "note")
    private String note;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getNote() {
        return "https://cmz-daigua.oss-cn-beijing.aliyuncs.com/" + note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
