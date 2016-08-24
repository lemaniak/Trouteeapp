package com.troutee.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.gson.Gson;
import com.troutee.R;
import com.troutee.activities.RegisterClientLocation;
import com.troutee.activities.TrouteeActivity;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by vicente on 11/03/16.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static  final SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy");

    public static NameValuePair createParam(String name, String value) {
        NameValuePair param = new NameValuePair();
        param.setName(name);
        param.setValue(value);
        return param;
    }

    public static Header createHeader(String name, String value) {
        Header header = new Header();
        header.setName(name);
        header.setValue(value);
        return header;
    }

    public static <T> RequestEntity createRequestEntity(T object) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(object);
            return new ByteArrayRequestEntity(json.getBytes(),
                    "application/json");
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        }
        return null;
    }

    public static void displayMessage(Context context,String message,MessageType messageType,PositionType positionType){
        if(context==null){
            throw new IllegalArgumentException("context required");
        }else if(message.isEmpty()){
            throw new IllegalArgumentException("message required");
        }else if(messageType==null){
            throw new IllegalArgumentException("message type is required");
        }else{
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout =inflater.inflate(R.layout.troutee_toast, null);
            TextView msg= (TextView) layout.findViewById(R.id.troutee_toast_msg);
            msg.setText(message);

            switch (messageType){
                case INFO:
                    layout.setBackground(ContextCompat.getDrawable(context,R.drawable.troutee_toast_info));
                    break;
                case ERROR:
                    layout.setBackground(ContextCompat.getDrawable(context,R.drawable.troutee_toast_error));
                    break;
                case SUCCESS:
                    layout.setBackground(ContextCompat.getDrawable(context,R.drawable.troutee_toast_success));
                    break;
                default:
                    break;
            }

            Toast trouteeToast=new Toast(context);
            trouteeToast.setView(layout);
            trouteeToast.setDuration(Toast.LENGTH_SHORT);
            switch (positionType){
                case TOP:
                    trouteeToast.setGravity(Gravity.TOP,0, 0);
                    break;
                case CENTER:
                    trouteeToast.setGravity(Gravity.CENTER,0, 0);
                    break;
                case BOTTON:
                    trouteeToast.setGravity(Gravity.BOTTOM ,0, 0);
                    break;
            }

            trouteeToast.show();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidLength(int min, int max, String text) {
        int length = text.length();
        if (length > min && length<max){
            return true;
        }
        return false;
    }



    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Size getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return new Size(width,height);
    }

    public static void shakeAndToast(Context context,View field, int stringid) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        field.startAnimation(shake);
        Utils.displayMessage(context, context.getString(stringid),MessageType.ERROR,PositionType.BOTTON);
    }

    public static boolean getLogged(String status){
        if(status.equalsIgnoreCase("logged")){
            return true;
        }else{
            return false;
        }
    }

    public static String getStringPreference(Context context,String preference){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(preference,null);
    }
    public static void setStringPreference(Context context,String preference,String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(preference, value).commit();
    }
    public static Boolean getBooleanPreference(Context context,String preference){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(preference, false);
    }

    public static void setBooleanPreference(Context context,String preference,boolean value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(preference, value).commit();
    }
    public static void setIntegerPreference(Context context,String preference,Integer value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(preference, value).commit();
    }
    public static void removePreference(Context context,String preference){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().remove(preference).commit();
    }
    public static Integer getIntegerPreference(Context context,String preference){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(preference, 0);
    }

    public static String dateToString(Date date){
        return format.format(date);
    }

    public static Date stringToDate(String date) throws  ParseException{
            return format.parse(date);
    }

    public static void requestLocationPermission(final Activity activity){

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Permission required")
                        .setMessage("You need to grant permission for geo location awareness")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }

                        })
                        .show();

            } else {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }

    }

    public static void requestCameraPermission(final Activity activity){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(activity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Permission required")
                    .setMessage("You need to grant permission to use device camera")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, Constants.MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);
                        }

                    })
                    .show();

        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},Constants.MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);
        }

    }

    public static boolean hasGSPpermission(Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            return false;
        }
    }

    public static boolean hasCamerapermission(Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            return false;
        }
    }

    public static LocationRequest createLocationRequest(long interval, long fastestInterval, int priority){
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(interval);
            mLocationRequest.setFastestInterval(fastestInterval);
            mLocationRequest.setPriority(priority);
        return mLocationRequest;
    }

    public static void checkLocationSettings(LocationRequest locationRequest,GoogleApiClient googleApiClient,TrouteeActivity activity){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(activity);
    }

    public static void checkLocationSettings(LocationRequest locationRequest,GoogleApiClient googleApiClient,RegisterClientLocation activity){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(activity);
    }

    @SuppressWarnings("deprecation")
    public static void turnGPSOff(Context context)
    {
        if(context!=null){
            String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider.contains("gps"))
            { //if gps is enabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                context.sendBroadcast(poke);
            }
        }
    }



    public static void takePicture(final Activity activity) {
        if(!hasCamerapermission(activity)){
            requestCameraPermission(activity);
        }else{
            final CharSequence[] items = { activity.getString(R.string.lbl_pick_capture_photo), activity.getString(R.string.lbl_pick_select_picture), activity.getString(R.string.lbl_cancel) };

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
            builder.setTitle(activity.getString(R.string.lbl_select_image));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(activity.getString(R.string.lbl_pick_capture_photo))) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(takePictureIntent, Constants.TAKE_PICTURE_CODE);
                    } else if (items[item].equals(activity.getString(R.string.lbl_pick_select_picture))) {
                        Intent selectImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(selectImageIntent, Constants.SELECT_IMAGE_CODE);
                    } else if (items[item].equals(activity.getString(R.string.lbl_cancel))) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }


    public static void cropImage(Activity activity,Uri imageUri){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(imageUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //start the activity - we handle returning in onActivityResult
            activity.startActivityForResult(cropIntent, Constants.CROP_IMAGE_CODE);
        }catch(Exception ex){
            Utils.displayMessage(activity,activity.getString(R.string.error_cropping_picture),MessageType.ERROR,PositionType.CENTER);
        }

    }

    public static boolean isDoubleClickSafe(Long lastClickTime){
        boolean result = SystemClock.elapsedRealtime() - lastClickTime > 1000;
        return result;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus()!=null){
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        } else if (unit == "MT"){
            dist= dist * 1609.34;
        }

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static long  getLocationSeconds(Location location){
        long elapsedTime=SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos();
        return TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
    }

}
