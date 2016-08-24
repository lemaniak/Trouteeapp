package com.troutee.dto.response;

import com.troutee.utils.WeekDays;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vicente on 24/06/16.
 */
public class RDevice implements Serializable{
    private Integer id;
    private String firstName;
    private String lastname;
    private String phoneNumber;
    private String vendorCode;
    private RegistrationStatus registrationStatus;
    private Integer startMonitorHour;
    private Integer endMonitorHour;
    private Integer startMonitorMinute;
    private Integer endMonitorMinute;
    private Integer monitorInterval;
    private ArrayList<WeekDays> weekDays;
    private String image;

    public RDevice() {
    }

    public RDevice(Integer id, String firstName, String lastname, String phoneNumber, String vendorCode, RegistrationStatus registrationStatus, Integer startMonitorHour, Integer endMonitorHour, Integer startMonitorMinute, Integer endMonitorMinute, Integer monitorInterval, ArrayList<WeekDays> weekDays, String image) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.vendorCode = vendorCode;
        this.registrationStatus = registrationStatus;
        this.startMonitorHour = startMonitorHour;
        this.endMonitorHour = endMonitorHour;
        this.startMonitorMinute = startMonitorMinute;
        this.endMonitorMinute = endMonitorMinute;
        this.monitorInterval = monitorInterval;
        this.weekDays = weekDays;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
