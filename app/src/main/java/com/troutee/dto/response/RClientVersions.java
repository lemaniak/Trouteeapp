package com.troutee.dto.response;

import java.util.List;

/**
 * Created by vicente on 30/06/16.
 */
public class RClientVersions {

    private List<RClientVersion> clientVersions;
    private String clientids;

    public RClientVersions() {
    }

    public RClientVersions(List<RClientVersion> clientVersions) {
        this.clientVersions = clientVersions;
    }

    public List<RClientVersion> getClientVersions() {
        return clientVersions;
    }

    public void setClientVersions(List<RClientVersion> clientVersions) {
        this.clientVersions = clientVersions;
    }

    public String getClientids() {
        return clientids;
    }

    public void setClientids(String clientids) {
        this.clientids = clientids;
    }
}
