package com.troutee.dto.response;

/**
 * Created by vicente on 16/03/16.
 */
public class RError extends Response{

    private int code;
    private String errorMessage;

    public RError() {
    }

    public RError(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
