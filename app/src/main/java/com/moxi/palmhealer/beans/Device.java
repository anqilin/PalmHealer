package com.moxi.palmhealer.beans;

import java.io.Serializable;

/**
 * Created by yinlu on 2016/4/26.
 */
public class Device implements Serializable {
    private static final long serialVersionUID = 105920024509483502L;

    public String deviceName_CN;
    public String deviceName_EN;
    public String device_Name;
    public String device_Pic;
    public String device_Logo;
    public String device_mac;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDeviceName_CN() {
        return deviceName_CN;
    }

    public void setDeviceName_CN(String deviceName_CN) {
        this.deviceName_CN = deviceName_CN;
    }

    public String getDeviceName_EN() {
        return deviceName_EN;
    }

    public void setDeviceName_EN(String deviceName_EN) {
        this.deviceName_EN = deviceName_EN;
    }

    public String getDevice_Name() {
        return device_Name;
    }

    public void setDevice_Name(String device_Name) {
        this.device_Name = device_Name;
    }

    public String getDevice_Pic() {
        return device_Pic;
    }

    public void setDevice_Pic(String device_Pic) {
        this.device_Pic = device_Pic;
    }

    public String getDevice_Logo() {
        return device_Logo;
    }

    public void setDevice_Logo(String device_Logo) {
        this.device_Logo = device_Logo;
    }

    public String getDevice_mac() {
        return device_mac;
    }

    public void setDevice_mac(String device_mac) {
        this.device_mac = device_mac;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceName_CN='" + deviceName_CN + '\'' +
                ", deviceName_EN='" + deviceName_EN + '\'' +
                ", device_Name='" + device_Name + '\'' +
                ", device_Pic='" + device_Pic + '\'' +
                ", device_Logo='" + device_Logo + '\'' +
                ", device_mac='" + device_mac + '\'' +
                '}';
    }
}
