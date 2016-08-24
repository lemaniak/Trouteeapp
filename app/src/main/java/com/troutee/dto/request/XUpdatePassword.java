package com.troutee.dto.request;

/**
 * Created by vicente on 11/03/16.
 */
public class XUpdatePassword {
    private Integer id;
    private String password;
    private String token;

    public XUpdatePassword() {
    }

    public XUpdatePassword(Integer id, String password, String token) {
        this.id = id;
        this.password = password;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
