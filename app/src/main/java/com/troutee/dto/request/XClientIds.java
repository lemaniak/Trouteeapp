package com.troutee.dto.request;

import java.util.List;

/**
 * Created by vicente on 30/06/16.
 */
public class XClientIds extends XToken{
    private List<Integer> clientids;

    public XClientIds() {
    }

    public XClientIds(List<Integer> clientids) {
        this.clientids = clientids;
    }

    public List<Integer> getClientids() {
        return clientids;
    }

    public void setClientids(List<Integer> clientids) {
        this.clientids = clientids;
    }
}
