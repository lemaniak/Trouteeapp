package com.troutee.tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.troutee.R;
import com.troutee.activities.LoginActivity;
import com.troutee.activities.RegisterClientLocation;
import com.troutee.activities.TrouteeActivity;
import com.troutee.api.ClientService;
import com.troutee.api.UserService;
import com.troutee.dto.request.XClient;
import com.troutee.dto.request.XLogin;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.services.SyncronizeClientsService;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

/**
 * Created by vicente on 18/03/16.
 */
public class UpdateClientAsyncTask extends AsyncTask<String,String,String>{

    private XClient xClient;
    private RClient client;
    private RegisterClientLocation registerClientLocation;
    private ProgressDialog progressBar;
    private Integer errormsg;
    private TrouteeDBHelper dbHelper;
    private GoogleMap map;

    public UpdateClientAsyncTask(RegisterClientLocation registerClientLocation, XClient xClient, GoogleMap map) {
        this.xClient = xClient;
        this.registerClientLocation = registerClientLocation;
        this.map=map;
    }

    protected void onPreExecute(){
        progressBar = new ProgressDialog(registerClientLocation);
        progressBar.setCancelable(true);
        progressBar.setMessage(registerClientLocation.getString(R.string.msg_updating_client_data));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(registerClientLocation, R.drawable.progress));
        progressBar.show();
    }


    @Override
    protected String doInBackground(String... params) {
        ClientService clientService = new ClientService();
        Response response= clientService.updateClient(xClient);
        if (response == null || response.getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            return "unknow_error";
        } else if (response.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            RError rError = (RError) response.getEntity();
            ErrorMapper errorMapper = new ErrorMapper();
            errormsg = errorMapper.errors.get(rError.getCode());
            if (errormsg == null) {//no error message to map
                errormsg = R.string.error_updating_client;
            }
            return "error";
        } else if (response.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
            RError rError = (RError) response.getEntity();
            ErrorMapper errorMapper = new ErrorMapper();
            errormsg = errorMapper.errors.get(rError.getCode());
            if (errormsg == null) {//no error message to map
                errormsg = R.string.error_updating_client;
            }
            return "error";
        } else if (response.getStatusCode() == HttpStatus.SC_OK) {
            //update client info in local database
            dbHelper = new TrouteeDBHelper(registerClientLocation);
            client = (RClient) response.getEntity();
            String lat = null;
            String lon = null;
            if (client.getLat() != null) {
                lat = client.getLat().toString();
            }
            if (client.getLon() != null) {
                lon = client.getLon().toString();
            }
            dbHelper.updateClient(client.getId(), client.getCode(), client.getName(), client.getPhone(), client.getStatus().getValue(), lat, lon, client.getVersion());
            return "success";
        }
        return null;
    }




    protected void onPostExecute(String result) {
        progressBar.dismiss();
        if(result.compareTo("unknow_error")==0){
            Utils.displayMessage(registerClientLocation.getApplicationContext(),registerClientLocation.getString(R.string.error_updating_client), MessageType.ERROR, PositionType.CENTER);
        }else if(result.compareTo("error")==0){
            Utils.displayMessage(registerClientLocation.getApplicationContext(),registerClientLocation.getString(errormsg),MessageType.ERROR,PositionType.CENTER);
        }else if(result.compareTo("success")==0){
            Utils.displayMessage(registerClientLocation.getApplicationContext(), registerClientLocation.getString(R.string.msg_update_client_data_success), MessageType.SUCCESS, PositionType.CENTER);
            LatLng latLng = new LatLng(client.getLat(),xClient.getLon());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            map.clear();
            map.setInfoWindowAdapter(null);
            Marker marker=map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(xClient.getName()));
            marker.hideInfoWindow();
            xClient=null;
        }
    }
}
