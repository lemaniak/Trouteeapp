package com.troutee.tasks;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.troutee.R;
import com.troutee.activities.RegisterClientLocation;
import com.troutee.activities.TrouteeActivity;
import com.troutee.api.ClientService;
import com.troutee.dto.request.XClient;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.Response;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.utils.ErrorMapper;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.HttpStatus;

/**
 * Created by vicente on 18/03/16.
 */
public class CheckinAsyncTask extends AsyncTask<Location,String,String>{

    private XClient xClient;
    private TrouteeActivity activity;
    private ProgressDialog progressBar;
    private Integer errormsg;
    private Location location;
    private GoogleMap map;

    public CheckinAsyncTask(TrouteeActivity activity, XClient xClient, GoogleMap map) {
        this.xClient = xClient;
        this.activity = activity;
        this.map=map;
    }

    protected void onPreExecute(){
        progressBar = new ProgressDialog(activity);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getString(R.string.msg_updating_client_data));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(activity, R.drawable.progress));
        progressBar.show();
    }


    @Override
    protected String doInBackground(Location... params) {
        ClientService clientService = new ClientService();
        location=params[0];
        Response response= clientService.checkin(xClient,location);
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
            return "success";
        }
        return null;
    }



    protected void onPostExecute(String result) {
        progressBar.dismiss();
        if(result.compareTo("unknow_error")==0){
            Utils.displayMessage(activity,activity.getString(R.string.error_updating_client), MessageType.ERROR, PositionType.CENTER);
        }else if(result.compareTo("error")==0){
            Utils.displayMessage(activity,activity.getString(errormsg),MessageType.ERROR,PositionType.CENTER);
        }else if(result.compareTo("success")==0){
            Utils.displayMessage(activity, activity.getString(R.string.msg_update_client_data_success), MessageType.SUCCESS, PositionType.CENTER);
            activity.selectedClientMarker.remove();
            activity.setxClient(null);
        }
    }
}
