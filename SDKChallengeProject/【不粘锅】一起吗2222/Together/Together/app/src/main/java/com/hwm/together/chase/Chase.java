package com.hwm.together.chase;

import android.graphics.drawable.Drawable;

public class Chase {
    private int id;
    private String name;
    private Drawable cover;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getCover() {
        return cover;
    }

    public void setCover(Drawable cover) {
        this.cover = cover;
    }
}
