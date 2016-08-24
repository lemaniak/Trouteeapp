package com.troutee.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.troutee.R;
import com.troutee.adapters.TrouteeInfoWindowAdapter;
import com.troutee.dto.request.XClient;
import com.troutee.dto.response.RUser;
import com.troutee.mappers.UserMapper;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.tasks.UpdateClientAsyncTask;
import com.troutee.utils.Constants;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

public class RegisterClientLocation extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<LocationSettingsResult>,LocationListener,OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = RegisterClientLocation.class.getSimpleName();
    private TrouteeDBHelper dbHelper;
    private XClient xClient;
    private GoogleMap map;
    private ProgressDialog progressBar;
    private RUser user= null;
    private  GoogleApiClient mGoogleApiClient;
    private Location currentLocation;
    private  boolean requestingLocationUpdates;
    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client_location);
        updateValuesFromBundle(savedInstanceState);
        dbHelper=new TrouteeDBHelper(this);
        currentLocation=null;
        setUserData();
        setClientData();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.register_client_location_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map=map;
        this.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.map.setInfoWindowAdapter(new TrouteeInfoWindowAdapter(this));
        this.map.setOnInfoWindowClickListener(this);
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(getDefaultLocation(), 10));
        if (Utils.hasGSPpermission(this)) {
            try {
                requestLocation();
            }catch (SecurityException ex){
                Log.e(TAG, ex.toString());
            }
        } else {
            Utils.requestLocationPermission(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.requestLocationPermission(this);
                    requestLocation();
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        requestingLocationUpdates=false;
        map.clear();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserData();
        setClientData();
        if (mGoogleApiClient.isConnected() && !requestingLocationUpdates) {
            startLocationUpdates();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates(false);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, connectionResult.getErrorMessage());
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(!requestingLocationUpdates){
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(Constants.REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates);
        savedInstanceState.putParcelable(Constants.LOCATION_KEY, currentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(Constants.REQUESTING_LOCATION_UPDATES_KEY)) {
                requestingLocationUpdates = savedInstanceState.getBoolean(Constants.REQUESTING_LOCATION_UPDATES_KEY);
            }

            // Update the value of mCurrentLocation from the Bundle and update the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(Constants.LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that
                // mCurrentLocationis not null.
                currentLocation = savedInstanceState.getParcelable(Constants.LOCATION_KEY);
            }

        }
    }


    protected void startLocationUpdate(LocationRequest locationRequest) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }catch(SecurityException ex){
            Log.e(TAG,ex.getMessage());
        }
    }



    @Override
    public void onResult(LocationSettingsResult result) {
        final com.google.android.gms.common.api.Status status = result.getStatus();
        final LocationSettingsStates locationSettingsStates= result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can
                // initialize location requests here.
                if(!requestingLocationUpdates){
                    startLocationUpdates();
                }
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(this, Constants.LOCATION_SETTINGS_CHECK);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way
                // to fix the settings so we won't show the dialog.
                unBlockUI();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            Toast toast = Toast.makeText(this, "error checking location settings", Toast.LENGTH_SHORT);
            toast.show();
            unBlockUI();
        }else if(resultCode == RESULT_OK){
            switch(requestCode) {
                case Constants.LOCATION_SETTINGS_CHECK:
                    if(!requestingLocationUpdates){
                        startLocationUpdates();
                    }
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location.isFromMockProvider() && !Utils.getBooleanPreference(this,Constants.LOCATION_MOCKING) ){
            Utils.displayMessage(this, getResources().getString(R.string.error_mock_coordinate), MessageType.ERROR, PositionType.CENTER);
            stopLocationUpdates(true);

        }else {
            if(currentLocation==null){
                currentLocation=location;
                LatLng latLng= new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }else if(location.getAccuracy()< currentLocation.getAccuracy()){//last location update is more accurate then update current location
                currentLocation=location;
            }

            if(currentLocation.getAccuracy()<=50){//is high accuracy location
                currentLocation.setAccuracy(50);
            }

                xClient.setLat(location.getLatitude());
                xClient.setLon(location.getLongitude());
                addMarker(currentLocation);
                unBlockUI();
        }


    }

    public void stopLocationUpdates(boolean turnOffGPS){
        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            requestingLocationUpdates=false;
            if(turnOffGPS){
                Utils.turnGPSOff(this);
            }
        }
    }
    public void startLocationUpdates(){
        startLocationUpdate(Utils.createLocationRequest(2000, 1000, LocationRequest.PRIORITY_HIGH_ACCURACY));
        requestingLocationUpdates=true;
    }

    private void requestLocation(){
        blockUI();
        LocationRequest locationRequest=Utils.createLocationRequest(2000, 1000, LocationRequest.PRIORITY_HIGH_ACCURACY);
        Utils.checkLocationSettings(locationRequest, getmGoogleApiClient(), this);
    }

    protected void blockUI(){
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage(this.getString(R.string.msg_updating));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.progress));
        progressBar.show();
    }

    protected void unBlockUI(){
        if(progressBar!=null){
            progressBar.dismiss();
        }
    }

    private void setUserData(){
        Cursor cursor = dbHelper.getLoggedUser();
        if(cursor.getCount()>=1) {
            user = UserMapper.getUserFromCursor(cursor);
        }
        if(cursor!=null){
            cursor.close();
        }
    }

    private void setClientData(){
        xClient= (XClient) getIntent().getSerializableExtra("client");
        xClient.setToken(user.getToken());
    }

    private void addMarker(Location location){
        if(xClient!=null){
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            map.clear();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            Marker marker=map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(xClient.getName()));
            marker.showInfoWindow();
            circle = this.map.addCircle(new CircleOptions().center(latLng).radius(location.getAccuracy()).strokeColor(getResources().getColor(R.color.translucent_light_blue)).fillColor(getResources().getColor(R.color.translucent_light_blue)));

        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Utils.hideSoftKeyboard(this);
        displayClientLocationUpdateConfirmaDialog();
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void requestLocationUpdates(View view){
        Utils.displayMessage(this,getString(R.string.start_location_updates),MessageType.INFO,PositionType.CENTER);
        requestLocation();
    }

    private void displayClientLocationUpdateConfirmaDialog(){
        new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.confirmation))
                .setMessage(this.getString(R.string.update_client_coordinates_confirmation_msg))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        new UpdateClientAsyncTask(RegisterClientLocation.this,xClient,map).execute();
                        stopLocationUpdates(true);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public XClient getxClient() {
        return xClient;
    }

    private LatLng getDefaultLocation(){
        return new LatLng(9.948218,-84.142112);

    }

    public void gotoCurrentLocation(View view){
        LatLng latLng= new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    public void stopLocationUpdates(View view){
        blockUI();
       stopLocationUpdates(true);
       Utils.displayMessage(this,getString(R.string.stop_location_updates),MessageType.INFO,PositionType.CENTER);
        unBlockUI();
    }

}
