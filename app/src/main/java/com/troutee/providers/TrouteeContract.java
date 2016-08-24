package com.troutee.providers;

import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by vicente on 11/03/16.
 */
public class TrouteeContract {
    public static final String DB_NAME = "troutee.db";
    public static final int DB_VERSION = 1;
    private static final String TAG = TrouteeContract.class.getSimpleName();


    public static class User {
        public static final String TABLE_NAME = "tuser";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_FIRSTNAME = "firstname";
        public static final String COLUMN_LASTNAME = "lastname";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_TOKEN = "token";
        public static final String COLUMN_LOGGED = "logged";
        public static final String COLUMN_LASTLOGIN = "last_login";
        public static final String COLUMN_REGISTRATION_DATE = "registration_date";
        public static final String COLUMN_TOTAL_DEVICES = "total_devices";
    }

    public static class Device {
        public static final String TABLE_NAME = "device";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_FIRSTNAME = "firstname";
        public static final String COLUMN_LASTNAME = "lastname";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_REGISTRATION_STATUS = "registration_status";
        public static final String COLUMN_START_MONITOR_HOUR = "start_monitor_hour";
        public static final String COLUMN_START_MONITOR_MINUTE = "start_monitor_minute";
        public static final String COLUMN_END_MONITOR_HOUR = "end_monitor_hour";
        public static final String COLUMN_END_MONITOR_MINUTE = "end_monitor_minute";
        public static final String COLUMN_MONITOR_INTERVAL = "monitor_interval";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_SUNDAY = "sunday";
        public static final String COLUMN_MONDAY = "monday";
        public static final String COLUMN_TUESDAY = "tuesday";
        public static final String COLUMN_WEDNESDAY = "wednesday";
        public static final String COLUMN_THURSDAY = "thursday";
        public static final String COLUMN_FRIDAY = "friday";
        public static final String COLUMN_SATURDAY = "saturday";
        public static final String COLUMN_USER_ID = "user_id";
    }

    public static final class Client {
        public static final String TABLE_NAME = "tclient";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_NAME = "cname";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LON = "lon";
        public static final String COLUMN_VERSION = "version";
    }


    public static final String generateUserTable() {
        String template = "CREATE TABLE %1$s (\n" +
                "    \"%2$s\" INTEGER PRIMARY KEY NOT NULL,\n" +
                "    \"%3$s\" TEXT NOT NULL,\n" +
                "    \"%4$s\" TEXT NOT NULL,\n" +
                "    \"%5$s\" TEXT NOT NULL,\n" +
                "    \"%6$s\" TEXT,\n" +
                "    \"%7$s\" TEXT NOT NULL,\n" +
                "    \"%8$s\" TEXT NOT NULL,\n" +
                "    \"%9$s\" TEXT,\n" +
                "    \"%10$s\" TEXT,\n" +
                "    \"%11$s\" INTEGER\n" +
                ");";
        String sql = String.format(template,
                User.TABLE_NAME,
                User.COLUMN_ID,
                User.COLUMN_FIRSTNAME,
                User.COLUMN_LASTNAME,
                User.COLUMN_EMAIL,
                User.COLUMN_IMAGE,
                User.COLUMN_TOKEN,
                User.COLUMN_LOGGED,
                User.COLUMN_LASTLOGIN,
                User.COLUMN_REGISTRATION_DATE,
                User.COLUMN_TOTAL_DEVICES);
        Log.d(TAG, "onCreate with SQL: " + sql);
        return sql;
    }

    public static final String generateClientTable() {
        String template = "CREATE TABLE %1$s (\n" +
                "    \"%2$s\" INTEGER PRIMARY KEY NOT NULL,\n" +
                "    \"%3$s\" TEXT NOT NULL,\n" +
                "    \"%4$s\" TEXT NOT NULL,\n" +
                "    \"%5$s\" TEXT,\n" +
                "    \"%6$s\" TEXT NOT NULL,\n" +
                "    \"%7$s\" TEXT,\n" +
                "    \"%8$s\" TEXT,\n" +
                "    \"%9$s\" INTEGER NOT NULL\n" +
                ");";
        String sql = String.format(template,
                Client.TABLE_NAME,
                Client.COLUMN_ID,
                Client.COLUMN_CODE,
                Client.COLUMN_NAME,
                Client.COLUMN_PHONE,
                Client.COLUMN_STATUS,
                Client.COLUMN_LAT,
                Client.COLUMN_LON,
                Client.COLUMN_VERSION);
        Log.d(TAG, "onCreate with SQL: " + sql);
        return sql;
    }

    public static final String generateDeviceTable() {
        String template = "CREATE TABLE %1$s (\n" +
                "    \"%2$s\" INTEGER PRIMARY KEY NOT NULL,\n" +
                "    \"%3$s\" TEXT NOT NULL,\n" +
                "    \"%4$s\" TEXT NOT NULL,\n" +
                "    \"%5$s\" TEXT,\n" +
                "    \"%6$s\" TEXT NOT NULL,\n" +
                "    \"%7$s\" INTEGER NOT NULL,\n" +
                "    \"%8$s\" INTEGER NOT NULL,\n" +
                "    \"%9$s\" INTEGER NOT NULL,\n" +
                "    \"%10$s\" INTEGER NOT NULL,\n" +
                "    \"%11$s\" INTEGER NOT NULL,\n" +
                "    \"%12$s\" TEXT, \n" +
                "    \"%13$s\" INTEGER  NOT NULL  DEFAULT (0), \n" +
                "    \"%14$s\" INTEGER  NOT NULL  DEFAULT (0), \n" +
                "    \"%15$s\" INTEGER  NOT NULL  DEFAULT (0), \n" +
                "    \"%16$s\" INTEGER  NOT NULL  DEFAULT (0), \n" +
                "    \"%17$s\" INTEGER  NOT NULL  DEFAULT (0), \n" +
                "    \"%18$s\" INTEGER  NOT NULL  DEFAULT (0), \n" +
                "    \"%19$s\" INTEGER  NOT NULL  DEFAULT (0),\n" +
                "    \"%20$s\" INTEGER  NOT NULL \n" +
                ");";
        String sql = String.format(template, TrouteeContract.Device.TABLE_NAME,
                Device.COLUMN_ID,
                Device.COLUMN_FIRSTNAME,
                Device.COLUMN_LASTNAME,
                Device.COLUMN_PHONE,
                Device.COLUMN_REGISTRATION_STATUS,
                Device.COLUMN_START_MONITOR_HOUR,
                Device.COLUMN_START_MONITOR_MINUTE,
                Device.COLUMN_END_MONITOR_MINUTE,
                Device.COLUMN_END_MONITOR_HOUR,
                Device.COLUMN_MONITOR_INTERVAL,
                Device.COLUMN_IMAGE,
                Device.COLUMN_SUNDAY,
                Device.COLUMN_MONDAY,
                Device.COLUMN_TUESDAY,
                Device.COLUMN_WEDNESDAY,
                Device.COLUMN_THURSDAY,
                Device.COLUMN_FRIDAY,
                Device.COLUMN_SATURDAY,
                Device.COLUMN_USER_ID);
        Log.d(TAG, "onCreate with SQL: " + sql);
        return sql;
    }

}
