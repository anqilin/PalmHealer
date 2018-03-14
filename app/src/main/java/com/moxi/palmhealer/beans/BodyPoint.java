package com.moxi.palmhealer.beans;

import java.io.Serializable;

/**
 * Created by yinlu on 2016/4/16.
 */
public class BodyPoint implements Serializable {

    private static final long serialVersionUID = 7945261266577330439L;
    public String pointName;
    public String picName;
    public String location;
    public String features;


    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "BodyPoint{" +
                "pointName=" + pointName +
                ", picName='" + picName + '\'' +
                ", location='" + location + '\'' +
                ", features='" + features + '\'' +
                '}';
    }
}
