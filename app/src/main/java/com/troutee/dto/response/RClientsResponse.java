package com.troutee.dto.response;

import java.util.List;

/**
 * Created by vicente on 28/06/16.
 */
public class RClientsResponse {
    List<RClient> clients;

    public RClientsResponse() {
    }

    public RClientsResponse(List<RClient> clients) {
        this.clients = clients;
    }

    public List<RClient> getClients() {
        return clients;
    }

    public void setClients(List<RClient> clients) {
        this.clients = clients;
    }
}
