package com.troutee.utils;

import android.os.Environment;

/**
 * Created by vicente on 04/04/16.
 */
public class Constants {

    public static final String AUTO_LOGGING="auto_logging";
    public static final String LOCATION_MOCKING="location_mocking";
    public static final String REQUESTING_LOCATION_UPDATES_KEY="requesting_location_updates";
    public static final String LOCATION_KEY="location_key";
    public static final String XDEVICE="xdevice";
//    public static final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/troutee/user.jpg";
    public static final String USERIMAGE = "user.jpg";
    public static final String TROUTEE_IMAGE_FOLDER = "/troutee";
    public static final String path_temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/troutee/temp.jpg";
    public final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    public final static int LOCATION_SETTINGS_CHECK = 101;
    public final static int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA= 102;
    public static final int SELECT_IMAGE_CODE = 0;
    public static final int TAKE_PICTURE_CODE = 1;
    public static final int CROP_IMAGE_CODE = 2;
    public static final boolean SCALE_IMAGE = true;
    public static final String EMAIL_ADDRESS = "trouteeapp@gmail.com";
    public static final String EMAIL_ADDRESS_ADMIN = "lema017@gmail.com";
    public static final String EMAIL_PASSWORD = "Vcnt1717";
}
