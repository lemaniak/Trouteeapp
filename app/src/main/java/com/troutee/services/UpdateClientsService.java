package com.troutee.services;

import android.app.IntentService;
import android.content.Intent;

import com.troutee.R;
import com.troutee.api.ClientService;
import com.troutee.api.UserService;
import com.troutee.dto.request.XToken;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RClientsResponse;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

/**
 * Created by vicente on 01/04/16.
 */
public class UpdateClientsService extends IntentService {

    private final String TAG = UpdateClientsService.class.getSimpleName();
    private TrouteeDBHelper dbHelper;

    public UpdateClientsService() {
        super("UpdateClientsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Retrieve User From DB
        RUser user = (RUser) intent.getSerializableExtra("user");
        if(user!=null && Utils.isOnline(this)){
            dbHelper=new TrouteeDBHelper(this);
            ClientService clientService = new ClientService();
            XToken xToken = new XToken(user.getToken());
            Response response=clientService.getAllClients(xToken);
            if(response.getStatusCode()== HttpStatus.SC_OK){
                RClientsResponse clients = (RClientsResponse) response.getEntity();
                if(clients!=null && clients.getClients()!=null){
                    for(RClient c :clients.getClients()){
                        dbHelper.insertClient(c.getId(),c.getCode(),c.getName(),c.getPhone(),c.getStatus().getValue(),c.getLat() != null? c.getLat() +"":null,c.getLon()!= null ? c.getLon()+"": null, c.getVersion());
                    }
                }
            }
        }
    }
}
