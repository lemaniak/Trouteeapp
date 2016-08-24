package com.troutee.dto.response;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vicente on 11/03/16.
 */
public class RUser implements Serializable{

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private String token;
    private Boolean logged;
    private String createdAt;
    private String lastLogin;
    private int totalDevices;

    public RUser() {
    }

    public RUser(int id, String firstName, String lastName, String email, String image, String token) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.token = token;
    }

    public RUser(int id, String firstName, String lastName, String email, String image, String token, Boolean logged) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.token = token;
        this.logged = logged;
    }

    public RUser(int id, String firstName, String lastName, String email, String image, String token, Boolean logged, String createdAt, String lastLogin, int totalDevices) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.token = token;
        this.logged = logged;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.totalDevices = totalDevices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(int totalDevices) {
        this.totalDevices = totalDevices;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
