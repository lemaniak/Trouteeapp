package com.troutee.dto.request;

import com.troutee.utils.WeekDays;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vicente on 16/06/16.
 */
public class XCreateDevice implements Serializable {

    private String firstName;
    private String lastname;
    private String phoneNumber;
    private String vendorCode;
    private Integer startMonitorHour;
    private Integer endMonitorHour;
    private Integer startMonitorMinute;
    private Integer endMonitorMinute;
    private Integer monitorInterval;
    private ArrayList<WeekDays> weekDays;
    private byte[] image;
    private String token;

    public XCreateDevice() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public Integer getStartMonitorHour() {
        return startMonitorHour;
    }

    public void setStartMonitorHour(Integer startMonitorHour) {
        this.startMonitorHour = startMonitorHour;
    }

    public Integer getEndMonitorHour() {
        return endMonitorHour;
    }

    public void setEndMonitorHour(Integer endMonitorHour) {
        this.endMonitorHour = endMonitorHour;
    }

    public Integer getStartMonitorMinute() {
        return startMonitorMinute;
    }

    public void setStartMonitorMinute(Integer startMonitorMinute) {
        this.startMonitorMinute = startMonitorMinute;
    }

    public Integer getEndMonitorMinute() {
        return endMonitorMinute;
    }

    public void setEndMonitorMinute(Integer endMonitorMinute) {
        this.endMonitorMinute = endMonitorMinute;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getMonitorInterval() {
        return monitorInterval;
    }

    public void setMonitorInterval(Integer monitorInterval) {
        this.monitorInterval = monitorInterval;
    }

    public ArrayList<WeekDays> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(ArrayList<WeekDays> weekDays) {
        this.weekDays = weekDays;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
