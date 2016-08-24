package com.troutee.dto.response;

/**
 * Created by vicente on 16/03/16.
 */
public class Response {
    private int statusCode;

    private Object entity;

    public Response() {
    }

    public Response(int statusCode, Object entity) {
        this.statusCode = statusCode;
        this.entity = entity;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }
}
