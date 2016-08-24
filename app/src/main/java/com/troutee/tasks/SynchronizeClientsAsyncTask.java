package com.troutee.tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.troutee.R;
import com.troutee.activities.LoginActivity;
import com.troutee.activities.TrouteeActivity;
import com.troutee.api.ClientService;
import com.troutee.api.UserService;
import com.troutee.dto.request.XClientIds;
import com.troutee.dto.request.XLogin;
import com.troutee.dto.request.XToken;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RClientVersion;
import com.troutee.dto.response.RClientVersions;
import com.troutee.dto.response.RClientsResponse;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.mappers.ClientMapper;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.services.SyncronizeClientsService;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by vicente on 18/03/16.
 */
public class SynchronizeClientsAsyncTask extends AsyncTask<String,String,String>{

    private TrouteeActivity trouteeActivity;
    private ProgressDialog progressBar;
    private Integer errormsg;
    private TrouteeDBHelper dbHelper;
    private RUser user;

    public SynchronizeClientsAsyncTask(TrouteeActivity trouteeActivity,RUser user) {
        this.trouteeActivity = trouteeActivity;
        this.user=user;
    }

    protected void onPreExecute(){
        progressBar = new ProgressDialog(trouteeActivity);
        progressBar.setCancelable(true);
        progressBar.setMessage(trouteeActivity.getString(R.string.msg_loading));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(trouteeActivity, R.drawable.progress));
        progressBar.show();
    }


    @Override
    protected String doInBackground(String... params) {
        if (user != null && Utils.isOnline(trouteeActivity)) {
            dbHelper = new TrouteeDBHelper(trouteeActivity);
            XClientIds newClients = new XClientIds();
            newClients.setClientids(new ArrayList<Integer>());
            XClientIds updateClients = new XClientIds();
            updateClients.setClientids(new ArrayList<Integer>());
            ClientService clientService = new ClientService();
            XToken xToken = new XToken(user.getToken());
            Response response = clientService.getClientVersions(xToken);
            if (response!=null && response.getStatusCode() == HttpStatus.SC_OK) {
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
                return "success";
            }else{
                return "unknow_error";
            }
        }else{
            return "";
        }
    }


    protected void onPostExecute(String result) {
        progressBar.dismiss();
        if(result.compareTo("unknow_error")==0){
            Utils.displayMessage(trouteeActivity.getApplicationContext(),trouteeActivity.getString(R.string.error_updating_client_data), MessageType.ERROR, PositionType.CENTER);
        }else if(result.compareTo("success")==0){
            Utils.displayMessage(trouteeActivity.getApplicationContext(), trouteeActivity.getString(R.string.msg_update_client_data_success), MessageType.SUCCESS, PositionType.CENTER);
        }
    }
}
