package com.troutee.dto.response;

/**
 * Created by vicente on 04/04/16.
 */
public class RToken {

    private String token;
    private Boolean valid;

    public RToken() {
    }

    public RToken(String token, Boolean valid) {
        this.token = token;
        this.valid = valid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
