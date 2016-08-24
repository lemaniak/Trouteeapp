package com.troutee.dto.response;

/**
 * Created by vicente on 30/06/16.
 */
public class RClientVersion {
    private Integer clientid;
    private Integer versionid;

    public RClientVersion() {
    }

    public RClientVersion(Integer clientid, Integer versionid) {
        this.clientid = clientid;
        this.versionid = versionid;
    }

    public Integer getClientid() {
        return clientid;
    }

    public void setClientid(Integer clientid) {
        this.clientid = clientid;
    }

    public Integer getVersionid() {
        return versionid;
    }

    public void setVersionid(Integer versionid) {
        this.versionid = versionid;
    }
}
