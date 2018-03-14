package com.moxi.palmhealer.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yinlu on 2016/4/12.
 */
public class Disease implements Serializable {
    private static final long serialVersionUID = -4729612952035468203L;
    public String name;
    public String point;
    public String care;
    public String picNum;
    public ArrayList<PicRele> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getCare() {
        return care;
    }

    public void setCare(String care) {
        this.care = care;
    }

    public String getPicNum() {
        return picNum;
    }

    public void setPicNum(String picNum) {
        this.picNum = picNum;
    }

    public ArrayList<PicRele> getList() {
        return list;
    }

    public void setList(ArrayList<PicRele> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "name='" + name + '\'' +
                ", point='" + point + '\'' +
                ", care='" + care + '\'' +
                ", picNum='" + picNum + '\'' +
                ", list=" + list +
                '}';
    }

    public static class PicRele implements Serializable {
        public String picName;
        public String rele;

        public String getPicName() {
            return picName;
        }

        public void setPicName(String picName) {
            this.picName = picName;
        }

        public String getRele() {
            return rele;
        }

        public void setRele(String rele) {
            this.rele = rele;
        }

        @Override
        public String toString() {
            return "PicRele{" +
                    "picName='" + picName + '\'' +
                    ", rele='" + rele + '\'' +
                    '}';
        }
    }
}
