package com.troutee.activities;


import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.troutee.adapters.CheckinInfoWindowAdapter;
import com.troutee.adapters.TrouteeInfoWindowAdapter;
import com.troutee.dto.request.XClient;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RUser;
import com.troutee.mappers.ClientMapper;
import com.troutee.mappers.UserMapper;
import com.troutee.providers.TrouteeDBHelper;
import com.troutee.services.LogoutService;
import com.troutee.tasks.CheckinAsyncTask;
import com.troutee.tasks.DownloadImageAsyncTask;
import com.troutee.tasks.SynchronizeClientsAsyncTask;
import com.troutee.utils.Constants;
import com.troutee.utils.ImageUtils;
import com.troutee.utils.MessageType;
import com.troutee.utils.PositionType;
import com.troutee.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrouteeActivity  extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<LocationSettingsResult>,LocationListener,OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = RegisterClientLocation.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigation;
    private TrouteeDBHelper dbHelper;
    private RUser user= null;
    private TextView user_full_name;
    private TextView user_email;
    private ImageView user_image;
    private GoogleMap map;
    private AutoCompleteTextView actv;
    private ProgressDialog progressBar;
    private XClient xClient;

    private  GoogleApiClient mGoogleApiClient;
    private  boolean requestingLocationUpdates;
    private  Location currentLocation;

    public Marker currentLocationMarker;
    public Marker selectedClientMarker;
    private Circle circle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troutee);
        updateValuesFromBundle(savedInstanceState);
        init();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        this.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.map.setInfoWindowAdapter(new CheckinInfoWindowAdapter(this));
        this.map.setOnInfoWindowClickListener(this);
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(getDefaultLocation(), 10));
        //has gps active
        if (Utils.hasGSPpermission(this)) {
            try {
                requestLocation();
            }catch (SecurityException ex){
                Log.e(TAG,ex.toString());
            }
        } else {
            Utils.requestLocationPermission(this);
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
        if(selectedClientMarker!=null){
            selectedClientMarker.remove();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserData();
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
            blockUI();
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
                    unBlockUI();
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
        unBlockUI();
        if(location.isFromMockProvider() && !Utils.getBooleanPreference(this,Constants.LOCATION_MOCKING) ){
            Utils.displayMessage(this, getResources().getString(R.string.error_mock_coordinate), MessageType.ERROR, PositionType.CENTER);
            stopLocationUpdates(true);
        }else{
            if(currentLocation==null){
                currentLocation=location;
                LatLng latLng= new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }else if(location.getAccuracy()< currentLocation.getAccuracy()){//last location update is more accurate then update current location
                currentLocation=location;
            }else if(Utils.getLocationSeconds(currentLocation)>= 20 && location.getAccuracy()<=500){//more than 20 seconds have passed since we update current location (no more accurate location provided) and last location update is at least 500 mts accurate
                currentLocation=location;
            }

            if(currentLocation.getAccuracy()<=50){//is high accuracy location
                currentLocation.setAccuracy(50);
            }


            addMarker(currentLocation);
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
        startLocationUpdate(Utils.createLocationRequest(5000, 5000, LocationRequest.PRIORITY_HIGH_ACCURACY));
        requestingLocationUpdates=true;
    }




    private void init() {
        currentLocation=null;
        dbHelper=new TrouteeDBHelper(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        processAutoComplete(dbHelper);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(TrouteeActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        navigation = (NavigationView) findViewById(R.id.navigation_view);

        setUserData();
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_1:
                        //syncronize clients
                        if(user!=null){
                            synchronizeClients(user);
                        }
                        break;
                    case R.id.navigation_item_logout:
                        new AlertDialog.Builder(TrouteeActivity.this)
                                .setTitle(TrouteeActivity.this.getString(R.string.confirmation))
                                .setMessage(TrouteeActivity.this.getString(R.string.log_out_confirmation))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dbHelper = new TrouteeDBHelper(TrouteeActivity.this);
                                        Cursor cursor = dbHelper.getLoggedUser();
                                        RUser user = null;
                                        if (cursor.getCount() >= 1) {
                                            user = UserMapper.getUserFromCursor(cursor);
                                            cursor.close();
                                            dbHelper.updateUserStatus(user.getId(), false);
                                        }

                                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                                        startMain.addCategory(Intent.CATEGORY_HOME);
                                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(startMain);

                                        Intent logoutIntent = new Intent(TrouteeActivity.this, LogoutService.class);
                                        logoutIntent.putExtra("user", user);
                                        startService(logoutIntent);

                                        TrouteeActivity.this.finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                        break;
                }
                return false;
            }
        });

        //init google services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void setUserData(){
        if(navigation !=null && navigation.getHeaderView(0)!=null){
            user_image= (CircleImageView) navigation.getHeaderView(0).findViewById(R.id.nav_header_profile_image);
            user_full_name = (TextView) navigation.getHeaderView(0).findViewById(R.id.user_full_name);
            user_email = (TextView) navigation.getHeaderView(0).findViewById(R.id.user_email);
        }

        Cursor cursor = dbHelper.getLoggedUser();
        if(cursor.getCount()>=1){
            user= UserMapper.getUserFromCursor(cursor);
            cursor.close();
            if(user.getImage()!=null && !user.getImage().isEmpty() && user_image!=null && !ImageUtils.getUserImageFile(this).exists()){
                new DownloadImageAsyncTask(user_image,TrouteeActivity.this).execute(user.getImage());
            }else if(ImageUtils.getUserImageFile(this).exists()){
                user_image.setImageBitmap(ImageUtils.getBitmapFromFile(ImageUtils.getUserImageFile(this)));
            }
            if(user_full_name!=null){
                user_full_name.setText(user.getFirstName()+" "+user.getLastName());
            }
            if(user_email!=null){
                user_email.setText(user.getEmail());
            }

        }
    }

    private void synchronizeClients(RUser rUser){
        new SynchronizeClientsAsyncTask(this,rUser).execute();
    }

    public void gotosettings(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user",user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    private void requestLocation(){
        LocationRequest locationRequest=Utils.createLocationRequest(5000, 5000, LocationRequest.PRIORITY_HIGH_ACCURACY);
        Utils.checkLocationSettings(locationRequest, getmGoogleApiClient(), this);
    }

    private void processAutoComplete(final TrouteeDBHelper dbHelper){
        actv= (AutoCompleteTextView) findViewById(R.id.troute_activity_filter_etxt);
        Cursor suggestionsCursor= dbHelper.getAllClientNamesAndCodes();
        if(suggestionsCursor.getCount()==0){
            displaySynchronizeClientsDialog();
        }else{
            final List<String> suggestions= ClientMapper.getClientSuggestions(suggestionsCursor);
            if(actv!=null && suggestions!=null && !suggestions.isEmpty()){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,suggestions);
                actv.setAdapter(adapter);
                actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int selectedPos = suggestions.indexOf((String) ((TextView) view).getText());
                        Cursor selectedClient=dbHelper.getClientByName(suggestions.get(selectedPos));
                        RClient client= ClientMapper.getClientFromCursor(selectedClient);
                        if(client!=null){
                            if(client.getLat()==null && client.getLon()==null){
                                xClient = new XClient(user.getToken(),client.getId(),client.getCode(),client.getName(),client.getPhone(),client.getStatus(),client.getLat(),client.getLon(),client.getVersion());
                                displayUpdateClientCoordinatesDialog();
                                Utils.hideSoftKeyboard(TrouteeActivity.this);
                            }else{
                                xClient = new XClient(user.getToken(),client.getId(),client.getCode(),client.getName(),client.getPhone(),client.getStatus(),client.getLat(),client.getLon(),client.getVersion());
                                addMarker(client);
                                if(currentLocationMarker!=null){
                                    addMarker(currentLocation);
                                }
                                Utils.hideSoftKeyboard(TrouteeActivity.this);
                            }
                            actv.setText(null);
                        }
                    }
                });
            }
        }
    }

    private void displaySynchronizeClientsDialog(){
        new AlertDialog.Builder(TrouteeActivity.this)
                .setTitle(TrouteeActivity.this.getString(R.string.confirmation))
                .setMessage(TrouteeActivity.this.getString(R.string.syncronize_clients_confirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(user!=null){
                            synchronizeClients(user);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void displayUpdateClientCoordinatesDialog(){
        new AlertDialog.Builder(TrouteeActivity.this)
                .setTitle(TrouteeActivity.this.getString(R.string.confirmation))
                .setMessage(TrouteeActivity.this.getString(R.string.update_client__coordinates_confirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent registerClientLocationIntent = new Intent(TrouteeActivity.this,RegisterClientLocation.class);
                        registerClientLocationIntent.putExtra("client",xClient);
                        startActivity(registerClientLocationIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    protected void blockUI(){
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage(this.getString(R.string.msg_loading));
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


    private void addMarker(RClient client){
        LatLng latLng = new LatLng(client.getLat(),client.getLon());
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        if (selectedClientMarker == null) {
            selectedClientMarker = this.map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_pin)).title(client.getName()));
        }else{
            selectedClientMarker.remove();
            selectedClientMarker=this.map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_pin)).title(client.getName()));
        }

    }

    public void addMarker(Location location){
        final LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        if(currentLocationMarker==null){
            currentLocationMarker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_circle)));
            currentLocationMarker.hideInfoWindow();
        }else{
            currentLocationMarker.setPosition(latLng);
        }

        if(circle==null){
            circle = this.map.addCircle(new CircleOptions().center(latLng).radius(location.getAccuracy()).strokeColor(getResources().getColor(R.color.translucent_light_blue)).fillColor(getResources().getColor(R.color.translucent_light_blue)));
        }else{
            circle.setCenter(latLng);
            circle.setRadius(currentLocation.getAccuracy());
        }

    }



    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker!=currentLocationMarker){
            Utils.hideSoftKeyboard(this);
            displayClientCheckinConfirmaDialog();
        }

    }

    private void displayClientCheckinConfirmaDialog(){
        new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.confirmation))
                .setMessage(this.getString(R.string.checkin_confirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (currentLocation != null && xClient != null && xClient.getLat() != null && xClient.getLon() != null) {
                            float [] dist = new float[1];
                            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), xClient.getLat(), xClient.getLon(),dist);
                            if (dist[0] <= currentLocation.getAccuracy()) {//if client location intersects current location accuracy
                                new CheckinAsyncTask(TrouteeActivity.this, xClient, map).execute(currentLocation);

                            } else {
                                Utils.displayMessage(TrouteeActivity.this, getString(R.string.error_not_near_checkin_point), MessageType.ERROR, PositionType.CENTER);
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    public void gotoCurrentLocation(View view){
        LatLng latLng= new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }


    public XClient getxClient() {
        return xClient;
    }

    public void setxClient(XClient xClient) {
        this.xClient = xClient;
    }

    private LatLng getDefaultLocation(){
        return new LatLng(9.948218,-84.142112);
    }

    public Marker getCurrentLocationMarker() {
        return currentLocationMarker;
    }

    public void setCurrentLocationMarker(Marker currentLocationMarker) {
        this.currentLocationMarker = currentLocationMarker;
    }
}
