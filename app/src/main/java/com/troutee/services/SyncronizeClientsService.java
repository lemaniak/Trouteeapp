package com.troutee.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import com.troutee.api.ClientService;
import com.troutee.dto.request.XClientIds;
import com.troutee.dto.request.XToken;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RClientVersion;
import com.troutee.dto.response.RClientVersions;
import com.troutee.dto.response.RClientsResponse;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.mappers.ClientMapper;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by vicente on 01/04/16.
 */
public class SyncronizeClientsService extends IntentService {

    private final String TAG = SyncronizeClientsService.class.getSimpleName();
    private TrouteeDBHelper dbHelper;

    public SyncronizeClientsService() {
        super("SyncronizeClientsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Retrieve User From DB
        RUser user = (RUser) intent.getSerializableExtra("user");
        if (user != null && Utils.isOnline(this)) {
            dbHelper = new TrouteeDBHelper(this);
            XClientIds newClients = new XClientIds();
            newClients.setClientids(new ArrayList<Integer>());
            XClientIds updateClients = new XClientIds();
            updateClients.setClientids(new ArrayList<Integer>());
            ClientService clientService = new ClientService();
            XToken xToken = new XToken(user.getToken());
            Response response = clientService.getClientVersions(xToken);
            if (response.getStatusCode() == HttpStatus.SC_OK) {
                RClientVersions clientsVersions = (RClientVersions) response.getEntity();
                if (clientsVersions != null && clientsVersions.getClientVersions() != null) {
                    Cursor dbClientsCursor = dbHelper.getAllClients();
                    HashMap<Integer, RClient> dbClients = ClientMapper.getClientMapFromCursor(dbClientsCursor);
                    HashMap<Integer, RClientVersion> serverClients = new HashMap<Integer, RClientVersion>();
                    dbClientsCursor.close();

                    for (RClientVersion c : clientsVersions.getClientVersions()) {
                        if (dbClients == null || !dbClients.containsKey(c.getClientid())) {//is new client
                            newClients.getClientids().add(c.getClientid());
                        } else {//is update client
                            RClient dbClient = dbClients.get(c.getClientid());
                            if (dbClient.getVersion() < c.getVersionid()) {
                                updateClients.getClientids().add(c.getClientid());
                            }
                        }
                        serverClients.put(c.getClientid(), c);
                    }

                    if (dbClients != null) {
                        Iterator iterator = dbClients.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry pair = (Map.Entry) iterator.next();
                            if (!serverClients.containsKey(pair.getKey())) {//client deleted
                                dbHelper.deleteClient((Integer) pair.getKey());
                            }
                        }
                    }

                    if (!newClients.getClientids().isEmpty()) {
                        newClients.setToken(xToken.getToken());
                        response = clientService.getClientByIds(newClients);
                        if (response!=null && response.getStatusCode() == HttpStatus.SC_OK) {
                            RClientsResponse newClientsResponse = (RClientsResponse) response.getEntity();
                            if (newClientsResponse != null && newClientsResponse.getClients() != null && !newClientsResponse.getClients().isEmpty()) {
                                for (RClient c : newClientsResponse.getClients()) {
                                    dbHelper.insertClient(c.getId(), c.getCode(), c.getName(), c.getPhone(), c.getStatus().getValue(), c.getLat() != null ? c.getLat() + "" : null, c.getLon() != null ? c.getLon() + "" : null, c.getVersion());
                                }
                            }
                        }
                    }

                    if (!updateClients.getClientids().isEmpty()) {
                        updateClients.setToken(xToken.getToken());
                        response = clientService.getClientByIds(updateClients);
                        if (response!=null && response.getStatusCode() == HttpStatus.SC_OK) {
                            RClientsResponse newClientsResponse = (RClientsResponse) response.getEntity();
                            if (newClientsResponse != null && newClientsResponse.getClients() != null && !newClientsResponse.getClients().isEmpty()) {
                                for (RClient c : newClientsResponse.getClients()) {
                                    dbHelper.updateClient(c.getId(), c.getCode(), c.getName(), c.getPhone(), c.getStatus().getValue(), c.getLat() != null ? c.getLat() + "" : null, c.getLon() != null ? c.getLon() + "" : null, c.getVersion());
                                }
                            }
                        }
                    }


                } else {//no clients on server
                    dbHelper.deleteAllClient();
                }
            }
        }
    }
}
