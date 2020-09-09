package com.xiaoyang.poweroperation.data.entity;

import cn.bmob.v3.BmobObject;

/**
 * ProjectName: powerOperation
 * CreateDate: 2020/8/27
 * ClassName: Line
 * Author: xiaoyangyan
 * note
 */
public class Line extends BmobObject {
    private String code;
    private String name;
    private String voltagelevel;
    private String materia;
    private String altitude;
    private String runstatus;
    private double longitude;
    private double latitude;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoltagelevel() {
        return voltagelevel;
    }

    public void setVoltagelevel(String voltagelevel) {
        this.voltagelevel = voltagelevel;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(String runstatus) {
        this.runstatus = runstatus;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
