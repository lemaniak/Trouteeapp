package com.troutee.dto.response;

import java.io.Serializable;

/**
 * Created by vicente on 24/06/16.
 */
public class RDevicerRegister implements Serializable {

    private String activationToken;
    private Integer userId;
    private RDevice rDevice;

    public RDevicerRegister() {
    }

    public RDevicerRegister(String activationToken, Integer userId, RDevice rDevice) {
        this.activationToken = activationToken;
        this.userId = userId;
        this.rDevice = rDevice;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public RDevice getrDevice() {
        return rDevice;
    }

    public void setrDevice(RDevice rDevice) {
        this.rDevice = rDevice;
    }
}
