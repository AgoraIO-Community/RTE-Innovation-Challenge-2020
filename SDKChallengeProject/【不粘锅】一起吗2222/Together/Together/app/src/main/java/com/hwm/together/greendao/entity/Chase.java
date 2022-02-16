package com.hwm.together.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Chase {
    @Id(autoincrement = true)
    private Long id;//id
    private String name;//名字
    private String coverPath;//封面

    @Generated(hash = 409939764)
    public Chase(Long id, String name, String coverPath) {
        this.id = id;
        this.name = name;
        this.coverPath = coverPath;
    }

    @Generated(hash = 622564340)
    public Chase() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return coverPath;
    }

    public void setCover(String cover) {
        this.coverPath = cover;
    }

    public String getCoverPath() {
        return this.coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
